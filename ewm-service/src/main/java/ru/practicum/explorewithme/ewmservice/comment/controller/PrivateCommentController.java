package ru.practicum.explorewithme.ewmservice.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.ewmservice.comment.dto.CommentDTO;
import ru.practicum.explorewithme.ewmservice.comment.mapper.CommentMapper;
import ru.practicum.explorewithme.ewmservice.comment.model.Comment;
import ru.practicum.explorewithme.ewmservice.comment.service.CommentService;

import javax.validation.Valid;

@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDTO.Response createComment(
            @PathVariable Integer userId,
            @RequestParam Integer eventId,
            @Valid @RequestBody CommentDTO.Request commentDTORequest
    ) {
        Comment createdComment = commentService.addComment(userId, eventId, CommentMapper.toComment(commentDTORequest));

        return CommentMapper.toCommentDTOResponse(createdComment);
    }

    @PatchMapping("/{commentId}")
    public CommentDTO.Response updateComment(
            @PathVariable Integer userId, @PathVariable Integer commentId,
            @Valid @RequestBody CommentDTO.Request commentDTORequest
    ) {
        Comment updatedComment = commentService
                .updateComment(userId, commentId, CommentMapper.toComment(commentDTORequest));

        return CommentMapper.toCommentDTOResponse(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Integer userId, @PathVariable Integer commentId) {
        commentService.deleteComment(userId, commentId);
    }
}
