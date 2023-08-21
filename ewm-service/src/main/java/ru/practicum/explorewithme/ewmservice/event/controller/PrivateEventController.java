package ru.practicum.explorewithme.ewmservice.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.ewmservice.category.dto.CategoryDTO;
import ru.practicum.explorewithme.ewmservice.event.annotation.CreateEventConstraint;
import ru.practicum.explorewithme.ewmservice.event.dto.EventDTO;
import ru.practicum.explorewithme.ewmservice.event.dto.EventRequestStatusResultDTO;
import ru.practicum.explorewithme.ewmservice.event.dto.EventRequestStatusUpdateRequestDTO;
import ru.practicum.explorewithme.ewmservice.event.mapper.EventMapper;
import ru.practicum.explorewithme.ewmservice.event.model.Event;
import ru.practicum.explorewithme.ewmservice.event.model.StateAction;
import ru.practicum.explorewithme.ewmservice.event.service.EventService;
import ru.practicum.explorewithme.ewmservice.request.dto.RequestDTO;
import ru.practicum.explorewithme.ewmservice.request.mapper.RequestMapper;
import ru.practicum.explorewithme.ewmservice.request.model.Request;
import ru.practicum.explorewithme.ewmservice.request.model.RequestStatus;
import ru.practicum.explorewithme.ewmservice.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}/events")
@Validated
@RequiredArgsConstructor
public class PrivateEventController {

    private final EventService eventService;
    private final RequestService requestService;

    @GetMapping
    public List<EventDTO<CategoryDTO>> getEventsByUserId(
            @PathVariable Integer userId,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size
    ) {
        return eventService.getEventsByUserId(userId, from, size)
                .stream().map(EventMapper::toEventDTO).collect(Collectors.toList());
    }

    @GetMapping("/{eventId}")
    public EventDTO<CategoryDTO> getUserEventById(@PathVariable Integer userId, @PathVariable Integer eventId) {
        Event event = eventService.getUserEventById(userId, eventId);

        return EventMapper.toEventDTO(event);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDTO<CategoryDTO> addEvent(@PathVariable Integer userId, @Valid @RequestBody @CreateEventConstraint EventDTO<Integer> eventDTO) {
        Event event = eventService.addEvent(userId, EventMapper.toEvent(eventDTO));

        return EventMapper.toEventDTO(event);
    }

    @PatchMapping("/{eventId}")
    public EventDTO<CategoryDTO> updateEvent(
            @PathVariable Integer userId, @PathVariable Integer eventId, @Valid @RequestBody EventDTO<Integer> eventDTO
    ) {
        StateAction stateAction = null;
        if (eventDTO.getStateAction() != null)
            stateAction = StateAction.from(eventDTO.getStateAction()).orElseThrow(() ->
                    new ValidationException("Неизвестное состояние события " + eventDTO.getStateAction())
            );

        Event event = eventService.updateEvent(userId, eventId, stateAction, EventMapper.toEvent(eventDTO));

        return EventMapper.toEventDTO(event);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDTO> getEventParticipants(@PathVariable Integer userId, @PathVariable Integer eventId) {
        return requestService.getEventParticipants(userId, eventId).stream()
                .map(RequestMapper::toRequestDTO).collect(Collectors.toList());
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusResultDTO updateRequests(
            @PathVariable Integer userId, @PathVariable Integer eventId,
            @Valid @RequestBody EventRequestStatusUpdateRequestDTO req
    ) {
        if (req.getStatus() != RequestStatus.CONFIRMED && req.getStatus() != RequestStatus.REJECTED)
            throw new ValidationException("Неизвестный статус запроса для изменения - " + req.getStatus().name());

        List<Request> requests = requestService.changeStatusOfAllRequests(
                userId, eventId, req.getRequestIds(), req.getStatus()
        );

        List<RequestDTO> confirmedRequests = new ArrayList<>();
        List<RequestDTO> rejectedRequests = new ArrayList<>();

        requests.forEach(request -> {
            if (request.getStatus() == RequestStatus.CONFIRMED)
                confirmedRequests.add(RequestMapper.toRequestDTO(request));
            if (request.getStatus() == RequestStatus.REJECTED)
                rejectedRequests.add(RequestMapper.toRequestDTO(request));
        });

        return new EventRequestStatusResultDTO(confirmedRequests, rejectedRequests);
    }

}
