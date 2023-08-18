package ru.practicum.explorewithme.ewmservice.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.ewmservice.request.model.RequestStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class RequestDTO {
    private int id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    private Integer event;
    private Integer requester;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private RequestStatus status;
}
