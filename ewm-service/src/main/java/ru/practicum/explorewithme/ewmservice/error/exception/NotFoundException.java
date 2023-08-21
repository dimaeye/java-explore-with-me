package ru.practicum.explorewithme.ewmservice.error.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
