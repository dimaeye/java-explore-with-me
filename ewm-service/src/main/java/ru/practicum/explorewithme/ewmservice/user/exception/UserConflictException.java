package ru.practicum.explorewithme.ewmservice.user.exception;

import ru.practicum.explorewithme.ewmservice.error.exception.ConflictException;

public class UserConflictException extends ConflictException {
    public UserConflictException(String message) {
        super(message);
    }
}
