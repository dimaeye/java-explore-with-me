package ru.practicum.explorewithme.ewmservice.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.ewmservice.comment.exception.CommentNotFoundException;
import ru.practicum.explorewithme.ewmservice.comment.model.Comment;
import ru.practicum.explorewithme.ewmservice.comment.repository.CommentRepository;
import ru.practicum.explorewithme.ewmservice.event.exception.BadEventStateException;
import ru.practicum.explorewithme.ewmservice.event.exception.EventNotFoundException;
import ru.practicum.explorewithme.ewmservice.event.model.Event;
import ru.practicum.explorewithme.ewmservice.event.model.EventState;
import ru.practicum.explorewithme.ewmservice.event.repository.EventRepository;
import ru.practicum.explorewithme.ewmservice.request.model.Request;
import ru.practicum.explorewithme.ewmservice.request.model.RequestStatus;
import ru.practicum.explorewithme.ewmservice.request.repository.RequestRepository;
import ru.practicum.explorewithme.ewmservice.user.exception.UserNotFoundException;
import ru.practicum.explorewithme.ewmservice.user.model.User;
import ru.practicum.explorewithme.ewmservice.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Comment addComment(int userId, int eventId, Comment comment) {
        User user = getUser(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId, userId));

        if (event.getState() != EventState.PUBLISHED)
            throw new BadEventStateException("Событие должно быть опубликовано");
        if (event.getInitiator().getId() == user.getId())
            throw new BadEventStateException("Пользователь не может оставлять комментарии к событию если он инициатор");

        Request request = requestRepository.findByEventIdAndRequesterId(eventId, userId)
                .orElseThrow(() -> new BadEventStateException("Запрос на участие в событие не найден"));
        if (request.getStatus() != RequestStatus.CONFIRMED)
            throw new BadEventStateException(
                    "Пользователь с id=" + userId + " не участвовал в событии с id=" + eventId
            );

        comment.setEvent(event);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment updateComment(int userId, int commentId, Comment comment) {
        Comment currentComment = getComment(userId, commentId);

        if (!comment.getText().isBlank())
            currentComment.setText(comment.getText());

        return commentRepository.save(currentComment);
    }

    @Override
    @Transactional
    public void deleteComment(int userId, int commentId) {
        Comment comment = getComment(userId, commentId);
        commentRepository.delete(comment);
    }

    @Override
    @Transactional
    public void deleteComment(int commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        commentRepository.deleteById(comment.getId());
    }

    @Override
    public List<Comment> getEventComments(int eventId, int from, int size) {
        return commentRepository.findAllByEventIdOrderByCreatedDesc(
                eventId, PageRequest.of(from / size, size)
        ).getContent();
    }

    private User getUser(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    private Comment getComment(int userId, int commentId) {
        return commentRepository.findByIdAndAuthorId(commentId, userId)
                .orElseThrow(() -> new CommentNotFoundException(commentId, userId));
    }
}
