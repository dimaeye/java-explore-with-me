package ru.practicum.explorewithme.ewmservice.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.ewmservice.category.dto.CategoryDTO;
import ru.practicum.explorewithme.ewmservice.category.mapper.CategoryMapper;
import ru.practicum.explorewithme.ewmservice.category.model.Category;
import ru.practicum.explorewithme.ewmservice.event.dto.EventDTO;
import ru.practicum.explorewithme.ewmservice.event.dto.EventShortDTO;
import ru.practicum.explorewithme.ewmservice.event.model.Event;
import ru.practicum.explorewithme.ewmservice.event.model.EventState;
import ru.practicum.explorewithme.ewmservice.user.mapper.UserMapper;

import java.util.Optional;

@UtilityClass
public class EventMapper {

    public EventDTO<CategoryDTO> toEventDTO(Event event) {
        return EventDTO.<CategoryDTO>builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDTO(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .location(LocationMapper.toLocationDTO(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .title(event.getTitle())
                .initiator(UserMapper.toUserDTO(event.getInitiator()))
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .state(event.getState())
                .views(event.getViews())
                .commentsCount(event.getCommentsCount())
                .build();
    }

    public EventShortDTO toEventShortDTO(Event event) {
        return EventShortDTO.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserDTO(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .commentsCount(event.getCommentsCount())
                .build();
    }

    public static Event toEvent(EventDTO<Integer> eventDTO) {
        Event.EventBuilder eventBuilder = Event.builder()
                .annotation(eventDTO.getAnnotation())
                .description(eventDTO.getDescription())
                .eventDate(eventDTO.getEventDate())
                .paid(eventDTO.getPaid())
                .participantLimit(eventDTO.getParticipantLimit())
                .requestModeration(eventDTO.getRequestModeration())
                .title(eventDTO.getTitle());

        if (eventDTO.getCategory() != null)
            eventBuilder.category(Category.builder().id(eventDTO.getCategory()).build());
        if (eventDTO.getLocation() != null)
            eventBuilder.location(LocationMapper.toLocation(eventDTO.getLocation()));

        return eventBuilder.build();
    }

    public static Optional<EventState> eventStateFromStr(String eventState) {
        for (EventState state : EventState.values()) {
            if (state.name().equalsIgnoreCase(eventState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
