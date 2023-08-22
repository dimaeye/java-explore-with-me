package ru.practicum.explorewithme.ewmservice.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.ewmservice.comment.dto.CommentDTO;
import ru.practicum.explorewithme.ewmservice.comment.mapper.CommentMapper;
import ru.practicum.explorewithme.ewmservice.comment.model.Comment;
import ru.practicum.explorewithme.ewmservice.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/users/{userId}/comments")
@Validated
@RequiredArgsConstructor
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDTO createComment(
            @PathVariable Integer userId,
            @NotNull(message = "Отсутствует id события в запросе")
            @RequestParam Integer eventId,
            @Valid @RequestBody CommentDTO commentDTO
    ) {
        Comment createdComment = commentService.addComment(userId, eventId, CommentMapper.toComment(commentDTO));

        return CommentMapper.toCommentDTO(createdComment);
    }

    @PatchMapping("/{commentId}")
    public CommentDTO updateComment(
            @PathVariable Integer userId, @PathVariable Integer commentId, @Valid @RequestBody CommentDTO commentDTO
    ) {
        Comment updatedComment = commentService.updateComment(userId, commentId, CommentMapper.toComment(commentDTO));

        return CommentMapper.toCommentDTO(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Integer userId, @PathVariable Integer commentId) {
        commentService.deleteComment(userId, commentId);
    }
}
