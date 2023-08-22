package ru.practicum.explorewithme.ewmservice.comment.exception;

import ru.practicum.explorewithme.ewmservice.error.exception.NotFoundException;

public class CommentNotFoundException extends NotFoundException {
    public CommentNotFoundException(int commentId) {
        super("Комментарий с id=" + commentId + " не найден");
    }

    public CommentNotFoundException(int commentId, int userId) {
        super("Комментарий с id=" + commentId + " не найден у пользователя id=" + userId);
    }
}
