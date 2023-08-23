package ru.practicum.explorewithme.ewmservice.comment.service;

import ru.practicum.explorewithme.ewmservice.comment.exception.CommentConflictException;
import ru.practicum.explorewithme.ewmservice.comment.exception.CommentNotFoundException;
import ru.practicum.explorewithme.ewmservice.comment.model.Comment;
import ru.practicum.explorewithme.ewmservice.event.exception.BadEventStateException;
import ru.practicum.explorewithme.ewmservice.event.exception.EventNotFoundException;
import ru.practicum.explorewithme.ewmservice.user.exception.UserNotFoundException;

import java.util.List;

public interface CommentService {
    //#region Private

    /**
     * @throws EventNotFoundException when event is not found
     * @throws UserNotFoundException  when user is not found
     * @throws BadEventStateException if eventState not PUBLISHED or user did not participate in the event
     */
    Comment addComment(int userId, int eventId, Comment comment);

    /**
     * @throws CommentNotFoundException when comment is not found
     * @throws CommentConflictException if disable edit comment by time
     */
    Comment updateComment(int userId, int commentId, Comment comment);

    /**
     * @throws CommentNotFoundException when comment is not found
     */
    void deleteComment(int userId, int commentId);

    //#endregion

    //#region Admin
    /**
     * @throws CommentNotFoundException when comment is not found
     */
    void deleteComment(int commentId);
    //#endregion

    //#region Public
    List<Comment> getEventComments(int eventId, int from, int size);
    //#endregion

}
