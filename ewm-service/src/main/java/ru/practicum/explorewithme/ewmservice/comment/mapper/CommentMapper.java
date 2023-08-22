package ru.practicum.explorewithme.ewmservice.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.ewmservice.comment.dto.CommentDTO;
import ru.practicum.explorewithme.ewmservice.comment.model.Comment;

@UtilityClass
public class CommentMapper {
    public static CommentDTO toCommentDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .eventId(comment.getEvent().getId())
                .authorId(comment.getAuthor().getId())
                .authorName(comment.getAuthor().getName())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }

    public static Comment toComment(CommentDTO commentDTO) {
        return Comment.builder()
                .text(commentDTO.getText())
                .build();
    }
}
