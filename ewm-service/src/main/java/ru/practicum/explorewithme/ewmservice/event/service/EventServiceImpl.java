package ru.practicum.explorewithme.ewmservice.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.ewmservice.category.exception.CategoryNotFoundException;
import ru.practicum.explorewithme.ewmservice.category.model.Category;
import ru.practicum.explorewithme.ewmservice.category.repository.CategoryRepository;
import ru.practicum.explorewithme.ewmservice.event.exception.BadEventStateException;
import ru.practicum.explorewithme.ewmservice.event.exception.EventNotFoundException;
import ru.practicum.explorewithme.ewmservice.event.model.Event;
import ru.practicum.explorewithme.ewmservice.event.model.EventState;
import ru.practicum.explorewithme.ewmservice.event.model.Location;
import ru.practicum.explorewithme.ewmservice.event.model.StateAction;
import ru.practicum.explorewithme.ewmservice.event.repository.EventRepository;
import ru.practicum.explorewithme.ewmservice.event.repository.LocationRepository;
import ru.practicum.explorewithme.ewmservice.user.exception.UserNotFoundException;
import ru.practicum.explorewithme.ewmservice.user.model.User;
import ru.practicum.explorewithme.ewmservice.user.repository.UserRepository;
import ru.practicum.explorewithme.stats.client.StatsClient;
import ru.practicum.explorewithme.stats.dto.HitDTO;
import ru.practicum.explorewithme.stats.dto.StatDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private static final String APP = "main-service";
    private static final String URI = "/events";

    private final EventRepository eventRepository;
    private final StatsClient statsClient;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Event> getEventsByUserId(int userId, int from, int size) {
        return eventRepository.findAllByInitiatorId(userId, PageRequest.of(from / size, size)).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public Event getUserEventById(int userId, int eventId) {
        return eventRepository.findEventByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new EventNotFoundException(eventId, userId));
    }

    @Override
    @Transactional
    public Event addEvent(int userId, Event event) {
        User user = getUser(userId);
        Category category = getCategory(event.getCategory().getId());

        event.setInitiator(user);
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        event.setConfirmedRequests(0);
        event.setLocation(getLocationWithId(event.getLocation()));
        event.setViews(0);
        if (event.getPaid() == null)
            event.setPaid(false);
        if (event.getParticipantLimit() == null)
            event.setParticipantLimit(0);
        if (event.getRequestModeration() == null)
            event.setRequestModeration(true);

        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public Event updateEvent(int userId, int eventId, StateAction stateAction, Event event) {
        User user = getUser(userId);
        Event currentEvent = eventRepository.findEventByIdAndInitiatorId(eventId, user.getId())
                .orElseThrow(() -> new EventNotFoundException(eventId, userId));
        if (currentEvent.getPublishedOn() != null)
            throw new BadEventStateException("Можно изменить события только в состоянии PENDING или CANCELED");

        if (event.getCategory() != null)
            currentEvent.setCategory(getCategory(event.getCategory().getId()));
        if (stateAction == StateAction.SEND_TO_REVIEW)
            currentEvent.setState(EventState.PENDING);
        else
            currentEvent.setState(EventState.CANCELED);

        return eventRepository.save(setDescriptionFields(event, currentEvent));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getAllEvents(FindEventsByAdminParams findEventsByAdminParams) {
        Pageable pageable = PageRequest.of(
                findEventsByAdminParams.getFrom() / findEventsByAdminParams.getSize(),
                findEventsByAdminParams.getSize()
        );

        return eventRepository.findAll(findEventsByAdminParams.getFilteredEvents(), pageable).getContent();
    }

    @Override
    @Transactional
    public Event updateEvent(int eventId, StateAction stateAction, Event event) {
        Event currentEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        if (event.getCategory() != null)
            currentEvent.setCategory(getCategory(event.getCategory().getId()));

        if (stateAction == StateAction.PUBLISH_EVENT) {
            if (currentEvent.getState() == EventState.PUBLISHED)
                throw new BadEventStateException("Событие уже опубликовано");
            else if (currentEvent.getState() == EventState.CANCELED)
                throw new BadEventStateException("Событие отменено");
            else {
                currentEvent.setState(EventState.PUBLISHED);
                currentEvent.setPublishedOn(LocalDateTime.now());
                currentEvent.setViews(0);
            }
        } else {
            if (currentEvent.getState() == EventState.PUBLISHED)
                throw new BadEventStateException("Событие уже опубликовано и не модет быть отменено");
            currentEvent.setState(EventState.CANCELED);
        }

        return eventRepository.save(setDescriptionFields(event, currentEvent));
    }

    @Override
    @Transactional
    public List<Event> getAllEvents(FindEventsByUserParams findEventsByUserParams, String ip) {
        Pageable pageable = PageRequest.of(
                findEventsByUserParams.getFrom() / findEventsByUserParams.getSize(),
                findEventsByUserParams.getSize()
        );

        List<Event> events = eventRepository.findAll(findEventsByUserParams.getFilteredEvents(), pageable).getContent();

        saveHit(ip, URI);
        Map<Integer, Event> publishedEvents = events.stream()
                .filter(event -> event.getState() == EventState.PUBLISHED)
                .collect(Collectors.toMap(Event::getId, Function.identity()));

        if (!publishedEvents.isEmpty()) {
            Map<Integer, StatDTO> stats = statsClient.getStats(
                    publishedEvents.values().stream().map(Event::getPublishedOn).sorted().findFirst().get(),
                    LocalDateTime.now(),
                    publishedEvents.keySet().stream().map(id -> URI + "/" + id).collect(Collectors.toList()),
                    true
            ).stream().collect(Collectors.toMap(
                    statDTO -> {
                        String[] splitUri = statDTO.getUri().split("/");
                        return Integer.valueOf(splitUri[splitUri.length - 1]);
                    }, Function.identity()
            ));

            publishedEvents.forEach((id, event) -> {
                saveHit(ip, URI + "/" + id);
                if (!stats.containsKey(id))
                    event.setViews(event.getViews() + 1);
            });
        }

        return events;
    }

    @Override
    @Transactional
    public Event getEventById(int eventId, String ip) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        if (event.getState() != EventState.PUBLISHED)
            throw new EventNotFoundException("Событие не опубликовано");

        String url = URI + "/" + eventId;
        List<StatDTO> stats = statsClient.getStats(
                event.getPublishedOn(), LocalDateTime.now(), List.of(url), true
        );
        saveHit(ip, url);
        if (stats.isEmpty())
            event.setViews(event.getViews() + 1);

        return event;
    }

    private User getUser(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    private Category getCategory(int categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

    private Event setDescriptionFields(Event from, Event to) {
        if (from.getAnnotation() != null)
            to.setAnnotation(from.getAnnotation());
        if (from.getTitle() != null)
            to.setTitle(from.getTitle());
        if (from.getLocation() != null)
            to.setLocation(getLocationWithId(from.getLocation()));
        if (from.getPaid() != null)
            to.setPaid(from.getPaid());
        if (from.getDescription() != null)
            to.setDescription(from.getDescription());
        if (from.getRequestModeration() != null)
            to.setRequestModeration(from.getRequestModeration());
        if (from.getParticipantLimit() != null)
            to.setParticipantLimit(from.getParticipantLimit());
        if (from.getEventDate() != null)
            to.setEventDate(from.getEventDate());
        return to;
    }

    private Location getLocationWithId(Location location) {
        return locationRepository.findByLatAndLon(location.getLat(), location.getLon())
                .orElseGet(() -> locationRepository.save(location));
    }

    private void saveHit(String ip, String url) {
        HitDTO hitDTO = HitDTO.builder()
                .ip(ip)
                .uri(url)
                .app(APP)
                .timestamp(LocalDateTime.now())
                .build();
        statsClient.saveReq(hitDTO);
    }

}
