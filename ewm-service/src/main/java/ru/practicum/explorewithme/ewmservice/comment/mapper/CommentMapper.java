package ru.practicum.explorewithme.ewmservice.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.ewmservice.comment.dto.CommentDTO;
import ru.practicum.explorewithme.ewmservice.comment.model.Comment;

@UtilityClass
public class CommentMapper {
    public CommentDTO.Response toCommentDTOResponse(Comment comment) {
        return CommentDTO.Response.builder()
                .id(comment.getId())
                .eventId(comment.getEvent().getId())
                .authorId(comment.getAuthor().getId())
                .authorName(comment.getAuthor().getName())
                .text(comment.getText())
                .created(comment.getCreated())
                .edited(comment.getEdited())
                .build();
    }

    public Comment toComment(CommentDTO.Request commentDTORequest) {
        return Comment.builder()
                .text(commentDTORequest.getText())
                .build();
    }
}
