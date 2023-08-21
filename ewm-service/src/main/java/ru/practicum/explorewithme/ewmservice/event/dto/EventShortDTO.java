package ru.practicum.explorewithme.ewmservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.ewmservice.user.dto.UserDTO;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class EventShortDTO {
    private int id;
    private String annotation;
    private Integer confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private UserDTO initiator;
    private Boolean paid;
    private String title;
    private Integer views;
}
