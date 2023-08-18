package ru.practicum.explorewithme.ewmservice.event.dto;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class LocationDTO {
    @NotNull
    private Float lat;
    @NotNull
    private Float lon;
}
