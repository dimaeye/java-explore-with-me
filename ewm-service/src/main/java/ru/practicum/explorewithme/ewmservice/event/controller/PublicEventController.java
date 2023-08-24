package ru.practicum.explorewithme.ewmservice.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.ewmservice.category.dto.CategoryDTO;
import ru.practicum.explorewithme.ewmservice.comment.dto.CommentDTO;
import ru.practicum.explorewithme.ewmservice.comment.mapper.CommentMapper;
import ru.practicum.explorewithme.ewmservice.comment.service.CommentService;
import ru.practicum.explorewithme.ewmservice.event.dto.EventDTO;
import ru.practicum.explorewithme.ewmservice.event.mapper.EventMapper;
import ru.practicum.explorewithme.ewmservice.event.model.Event;
import ru.practicum.explorewithme.ewmservice.event.model.SortParam;
import ru.practicum.explorewithme.ewmservice.event.service.EventService;
import ru.practicum.explorewithme.ewmservice.event.service.FindEventsByUserParams;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/events")
@Validated
@RequiredArgsConstructor
public class PublicEventController {

    private final EventService eventService;
    private final CommentService commentService;

    @GetMapping
    public List<EventDTO<CategoryDTO>> getAllEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false, name = "rangeStart")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startLocal,
            @RequestParam(required = false, name = "rangeEnd")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endLocal,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false, name = "sort") String sortParam,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        SortParam sort = null;
        if (sortParam != null) {
            sort = SortParam.from(sortParam).orElseThrow(() ->
                    new ValidationException("Неизвестный параметр сортировки " + sortParam)
            );
        }
        if (startLocal != null && endLocal != null && endLocal.isBefore(startLocal))
            throw new ValidationException("rangeEnd is before rangeStart");

        FindEventsByUserParams findEventsByUserParams = FindEventsByUserParams.builder()
                .sort(sort)
                .text(text)
                .categories(categories)
                .paid(paid)
                .startLocal(startLocal)
                .endLocal(endLocal)
                .onlyAvailable(onlyAvailable)
                .from(from)
                .size(size)
                .build();

        List<Event> events = eventService.getAllEvents(findEventsByUserParams, request.getRemoteAddr());
        return events.stream().map(EventMapper::toEventDTO).collect(Collectors.toList());
    }

    @GetMapping("/{eventId}")
    public EventDTO<CategoryDTO> getEventById(@PathVariable Integer eventId, HttpServletRequest request) {
        Event event = eventService.getEventById(eventId, request.getRemoteAddr());
        return EventMapper.toEventDTO(event);
    }

    @GetMapping("/{eventId}/comments")
    public List<CommentDTO.Response> getEventComments(
            @PathVariable Integer eventId,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size
    ) {
        return commentService.getEventComments(eventId, from, size).stream()
                .map(CommentMapper::toCommentDTOResponse).collect(Collectors.toList());
    }
}
