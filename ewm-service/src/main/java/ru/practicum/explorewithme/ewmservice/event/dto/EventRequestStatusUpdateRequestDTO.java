package ru.practicum.explorewithme.ewmservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.ewmservice.request.model.RequestStatus;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateRequestDTO {
    @NotNull
    private List<Integer> requestIds;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private RequestStatus status;
}
