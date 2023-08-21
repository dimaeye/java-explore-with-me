package ru.practicum.explorewithme.ewmservice.error.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}