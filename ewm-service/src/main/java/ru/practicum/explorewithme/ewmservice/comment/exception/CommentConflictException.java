package ru.practicum.explorewithme.ewmservice.comment.exception;

import ru.practicum.explorewithme.ewmservice.error.exception.ConflictException;

public class CommentConflictException extends ConflictException {
    public CommentConflictException(String message) {
        super(message);
    }
}
