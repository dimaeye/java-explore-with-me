package ru.practicum.explorewithme.ewmservice.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.ewmservice.request.dto.RequestDTO;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class EventRequestStatusResultDTO {
    private List<RequestDTO> confirmedRequests;
    private List<RequestDTO> rejectedRequests;
}
