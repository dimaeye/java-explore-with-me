package ru.practicum.explorewithme.ewmservice.request.exception;

import ru.practicum.explorewithme.ewmservice.error.exception.ConflictException;

public class BadRequestStateException extends ConflictException {
    public BadRequestStateException(String message) {
        super(message);
    }
}
