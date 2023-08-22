package ru.practicum.explorewithme.ewmservice.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.ewmservice.comment.model.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Optional<Comment> findByIdAndAuthorId(int commentId, int userId);

    Page<Comment> findAllByEventIdOrderByCreatedDesc(int eventId, Pageable pageable);
}
