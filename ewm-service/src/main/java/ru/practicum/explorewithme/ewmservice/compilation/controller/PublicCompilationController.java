package ru.practicum.explorewithme.ewmservice.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.ewmservice.compilation.dto.CompilationDTO;
import ru.practicum.explorewithme.ewmservice.compilation.mapper.CompilationMapper;
import ru.practicum.explorewithme.ewmservice.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/compilations")
@Validated
@RequiredArgsConstructor
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDTO> getCompilations(
            @RequestParam(required = false) Boolean pinned,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size
    ) {
        return compilationService.getCompilations(pinned, from, size)
                .stream().map(CompilationMapper::toCompilationDTO).collect(Collectors.toList());
    }

    @GetMapping("/{compilationId}")
    public CompilationDTO getCompilation(@PathVariable Integer compilationId) {
        return CompilationMapper.toCompilationDTO(compilationService.getCompilation(compilationId));
    }
}
