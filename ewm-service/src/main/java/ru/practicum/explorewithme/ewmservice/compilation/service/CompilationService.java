package ru.practicum.explorewithme.ewmservice.compilation.service;

import ru.practicum.explorewithme.ewmservice.compilation.model.Compilation;

import java.util.List;

public interface CompilationService {
    Compilation createCompilation(Compilation compilation);

    void deleteCompilation(int compilationId);

    Compilation updateCompilation(int compilationId, Compilation compilation);

    List<Compilation> getCompilations(boolean pinned, int from, int size);

    Compilation getCompilation(int compilationId);
}