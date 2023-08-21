package ru.practicum.explorewithme.ewmservice.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.ewmservice.compilation.dto.CompilationDTO;
import ru.practicum.explorewithme.ewmservice.compilation.dto.EditableCompilationDTO;
import ru.practicum.explorewithme.ewmservice.compilation.model.Compilation;
import ru.practicum.explorewithme.ewmservice.event.mapper.EventMapper;
import ru.practicum.explorewithme.ewmservice.event.model.Event;

import java.util.Collections;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {
    public static CompilationDTO toCompilationDTO(Compilation compilation) {
        return CompilationDTO.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned() != null ? compilation.getPinned() : false)
                .title(compilation.getTitle())
                .events(
                        compilation.getEvents() != null ? compilation.getEvents().stream()
                                .map(EventMapper::toEventShortDTO)
                                .collect(Collectors.toList()) : Collections.emptyList()
                )
                .build();
    }

    public static Compilation toCompilation(EditableCompilationDTO editableCompilationDTO) {
        Compilation.CompilationBuilder compilationBuilder =
                Compilation.builder()
                        .pinned(editableCompilationDTO.getPinned() != null ? editableCompilationDTO.getPinned() : false)
                        .title(editableCompilationDTO.getTitle());

        if (editableCompilationDTO.getEvents() != null && !editableCompilationDTO.getEvents().isEmpty())
            compilationBuilder.events(
                    editableCompilationDTO.getEvents()
                            .stream().map(eventId -> Event.builder().id(eventId).build())
                            .collect(Collectors.toSet())
            );

        return compilationBuilder.build();
    }

}
