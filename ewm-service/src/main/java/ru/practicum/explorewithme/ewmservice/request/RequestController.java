package ru.practicum.explorewithme.ewmservice.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.ewmservice.request.dto.RequestDTO;
import ru.practicum.explorewithme.ewmservice.request.mapper.RequestMapper;
import ru.practicum.explorewithme.ewmservice.request.service.RequestService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @GetMapping
    public List<RequestDTO> getAllUserRequests(@PathVariable Integer userId) {
        return requestService.getAllUserRequests(userId)
                .stream().map(RequestMapper::toRequestDTO).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDTO add(
            @PathVariable Integer userId,
            @RequestParam Integer eventId
    ) {
        return RequestMapper.toRequestDTO(requestService.addParticipationRequest(userId, eventId));
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDTO cancelRequest(@PathVariable Integer userId, @PathVariable Integer requestId) {
        return RequestMapper.toRequestDTO(requestService.cancelRequest(userId, requestId));
    }
}
