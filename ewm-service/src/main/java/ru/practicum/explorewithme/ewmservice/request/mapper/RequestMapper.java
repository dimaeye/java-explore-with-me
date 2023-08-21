package ru.practicum.explorewithme.ewmservice.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.ewmservice.request.dto.RequestDTO;
import ru.practicum.explorewithme.ewmservice.request.model.Request;

@UtilityClass
public class RequestMapper {
    public static RequestDTO toRequestDTO(Request request) {
        return RequestDTO.builder()
                .id(request.getId())
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .status(request.getStatus())
                .created(request.getCreated())
                .build();
    }
}
