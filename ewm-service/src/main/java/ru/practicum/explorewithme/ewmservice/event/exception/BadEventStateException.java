package ru.practicum.explorewithme.ewmservice.event.exception;

import ru.practicum.explorewithme.ewmservice.error.exception.ConflictException;

public class BadEventStateException extends ConflictException {
    public BadEventStateException(String message) {
        super(message);
    }
}
