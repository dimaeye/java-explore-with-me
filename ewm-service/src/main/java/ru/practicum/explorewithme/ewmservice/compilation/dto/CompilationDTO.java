package ru.practicum.explorewithme.ewmservice.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.ewmservice.event.dto.EventShortDTO;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CompilationDTO {
    private int id;
    private boolean pinned;
    private String title;
    private List<EventShortDTO> events;
}
