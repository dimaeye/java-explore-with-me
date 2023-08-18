package ru.practicum.explorewithme.ewmservice.category.exception;

import ru.practicum.explorewithme.ewmservice.error.exception.ConflictException;

public class CategoryChangesException extends ConflictException {
    public CategoryChangesException(String message) {
        super(message);
    }
}
