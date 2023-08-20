package ru.practicum.explorewithme.ewmservice.compilation.service;

import ru.practicum.explorewithme.ewmservice.compilation.exception.CompilationNotFoundException;
import ru.practicum.explorewithme.ewmservice.compilation.model.Compilation;

import java.util.List;

public interface CompilationService {
    Compilation createCompilation(Compilation compilation);

    /**
     * @throws CompilationNotFoundException when the compilation is not found
     */
    void deleteCompilation(int compilationId);

    /**
     * @throws CompilationNotFoundException when the compilation is not found
     */
    Compilation updateCompilation(int compilationId, Compilation compilation);

    List<Compilation> getCompilations(boolean pinned, int from, int size);

    /**
     * @throws CompilationNotFoundException when the compilation is not found
     */
    Compilation getCompilation(int compilationId);
}