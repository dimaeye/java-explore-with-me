package ru.practicum.explorewithme.ewmservice.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.ewmservice.compilation.dto.CompilationDTO;
import ru.practicum.explorewithme.ewmservice.compilation.dto.EditableCompilationDTO;
import ru.practicum.explorewithme.ewmservice.compilation.mapper.CompilationMapper;
import ru.practicum.explorewithme.ewmservice.compilation.model.Compilation;
import ru.practicum.explorewithme.ewmservice.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.ValidationException;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDTO add(@Valid @RequestBody EditableCompilationDTO editableCompilationDTO) {
        if (editableCompilationDTO.getTitle() == null || editableCompilationDTO.getTitle().isBlank())
            throw new ValidationException("Название подборки не может быть пустым");
        Compilation compilation =
                compilationService.createCompilation(CompilationMapper.toCompilation(editableCompilationDTO));

        return CompilationMapper.toCompilationDTO(compilation);
    }

    @PatchMapping("/{compilationId}")
    public CompilationDTO patch(
            @PathVariable Integer compilationId, @Valid @RequestBody EditableCompilationDTO editableCompilationDTO
    ) {
        Compilation compilation = compilationService
                .updateCompilation(compilationId, CompilationMapper.toCompilation(editableCompilationDTO));

        return CompilationMapper.toCompilationDTO(compilation);
    }

    @DeleteMapping("/{compilationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer compilationId) {
        compilationService.deleteCompilation(compilationId);
    }
}
