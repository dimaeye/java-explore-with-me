package ru.practicum.explorewithme.ewmservice.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.ewmservice.category.dto.CategoryDTO;
import ru.practicum.explorewithme.ewmservice.event.dto.EventDTO;
import ru.practicum.explorewithme.ewmservice.event.mapper.EventMapper;
import ru.practicum.explorewithme.ewmservice.event.model.Event;
import ru.practicum.explorewithme.ewmservice.event.model.EventState;
import ru.practicum.explorewithme.ewmservice.event.model.StateAction;
import ru.practicum.explorewithme.ewmservice.event.service.EventService;
import ru.practicum.explorewithme.ewmservice.event.service.FindEventsByAdminParams;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/events")
@Validated
@RequiredArgsConstructor
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventDTO<CategoryDTO>> getAllEvents(
            @RequestParam(required = false) List<Integer> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false, name = "rangeStart")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startLocal,
            @RequestParam(required = false, name = "rangeEnd")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endLocal,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size
    ) {
        if (states != null)
            states.forEach(state ->
                    EventState.from(state).orElseThrow(() ->
                            new ValidationException("Неизвестное состояние события " + state)
                    )
            );

        FindEventsByAdminParams findEventsByAdminParams = FindEventsByAdminParams.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .startLocal(startLocal)
                .endLocal(endLocal)
                .from(from)
                .size(size)
                .build();

        List<Event> events = eventService.getAllEvents(findEventsByAdminParams);

        return events.stream().map(EventMapper::toEventDTO).collect(Collectors.toList());
    }

    @PatchMapping("/{eventId}")
    public EventDTO<CategoryDTO> updateEvent(@PathVariable Integer eventId, @RequestBody @Valid EventDTO<Integer> eventDTO) {
        StateAction stateAction = null;
        if (eventDTO.getStateAction() != null)
            stateAction = StateAction.from(eventDTO.getStateAction()).orElseThrow(() ->
                    new ValidationException("Неизвестное состояние события " + eventDTO.getStateAction())
            );

        Event event = eventService.updateEvent(eventId, stateAction, EventMapper.toEvent(eventDTO));

        return EventMapper.toEventDTO(event);
    }
}
