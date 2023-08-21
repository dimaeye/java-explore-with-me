package ru.practicum.explorewithme.ewmservice.compilation.exception;

import ru.practicum.explorewithme.ewmservice.error.exception.NotFoundException;

public class CompilationNotFoundException extends NotFoundException {
    public CompilationNotFoundException(int compilationId) {
        super("Подборка с id=" + compilationId + " не найден!");
    }
}
