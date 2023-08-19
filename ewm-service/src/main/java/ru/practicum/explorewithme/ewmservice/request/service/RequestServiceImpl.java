package ru.practicum.explorewithme.ewmservice.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.ewmservice.event.exception.EventNotFoundException;
import ru.practicum.explorewithme.ewmservice.event.model.Event;
import ru.practicum.explorewithme.ewmservice.event.model.EventState;
import ru.practicum.explorewithme.ewmservice.event.repository.EventRepository;
import ru.practicum.explorewithme.ewmservice.request.exception.BadRequestStateException;
import ru.practicum.explorewithme.ewmservice.request.exception.RequestNotFoundException;
import ru.practicum.explorewithme.ewmservice.request.model.Request;
import ru.practicum.explorewithme.ewmservice.request.model.RequestStatus;
import ru.practicum.explorewithme.ewmservice.request.repository.RequestRepository;
import ru.practicum.explorewithme.ewmservice.user.exception.UserNotFoundException;
import ru.practicum.explorewithme.ewmservice.user.model.User;
import ru.practicum.explorewithme.ewmservice.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Request addParticipationRequest(int userId, int eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        if (requestRepository.findByEventIdAndRequesterId(eventId, userId).isPresent())
            throw new BadRequestStateException("Запрос уже создан");

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        if (event.getInitiator().getId() == userId)
            throw new BadRequestStateException("Инициатор события не может создать запрос на участие");
        if (event.getState() != EventState.PUBLISHED)
            throw new BadRequestStateException("Нельзя участвовать в неопубликованном событии");
        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests() >= event.getParticipantLimit())
            throw new BadRequestStateException("У события достигнут лимит запросов на участие");
        if (event.getEventDate().isBefore(LocalDateTime.now()))
            throw new BadRequestStateException("Событие уже началось");

        Request.RequestBuilder requestBuilder = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user);

        if (event.getRequestModeration())
            requestBuilder.status(RequestStatus.PENDING);
        else {
            requestBuilder.status(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }

        return requestRepository.save(requestBuilder.build());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Request> getAllUserRequests(int userId) {
        return requestRepository.findAllByRequesterId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Request> getEventParticipants(int userId, int eventId) {
        Optional<Event> optionalEvent = eventRepository.findEventByIdAndInitiatorId(eventId, userId);

        if (optionalEvent.isPresent())
            return requestRepository.findAllByEventId(eventId);
        else
            return Collections.emptyList();
    }

    @Override
    @Transactional
    public Request cancelRequest(int userId, int requestId) {
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new RequestNotFoundException(requestId));

        request.setStatus(RequestStatus.CANCELED);

        return requestRepository.save(request);
    }

    @Override
    @Transactional
    public List<Request> changeStatusOfAllRequests(
            int userId, int eventId, List<Integer> requestIds, RequestStatus requestStatus
    ) {
        Event event = eventRepository.findEventByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new EventNotFoundException(eventId, userId));

        if (event.getParticipantLimit() == 0 || !event.getRequestModeration())
            return Collections.emptyList();
        if (event.getConfirmedRequests() >= event.getParticipantLimit())
            throw new BadRequestStateException("Превышен лимит участия");


        List<Request> requests = requestRepository.findAllById(requestIds);

        if (requests.stream().anyMatch(request -> request.getStatus() != RequestStatus.PENDING))
            throw new BadRequestStateException(
                    "Статус можно изменить только у заявок, находящихся в состоянии ожидания"
            );

        if (requestStatus == RequestStatus.CONFIRMED)
            requests.forEach(request -> {
                if (event.getConfirmedRequests() <= event.getParticipantLimit()) {
                    request.setStatus(RequestStatus.CONFIRMED);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                } else
                    request.setStatus(RequestStatus.REJECTED);
            });
        else
            requests.forEach(request ->
                    request.setStatus(RequestStatus.REJECTED)
            );


        return requests;
    }
}
