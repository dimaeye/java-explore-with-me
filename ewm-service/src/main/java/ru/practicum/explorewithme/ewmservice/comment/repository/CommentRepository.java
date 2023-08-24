package ru.practicum.explorewithme.ewmservice.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.ewmservice.comment.model.Comment;
import ru.practicum.explorewithme.ewmservice.comment.model.ICommentCount;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Optional<Comment> findByIdAndAuthorId(int commentId, int userId);

    Page<Comment> findAllByEventIdOrderByCreatedDesc(int eventId, Pageable pageable);

    @Query("SELECT c.event.id AS eventId, COUNT(c.id) AS totalComment " +
            "FROM Comment AS c " +
            "WHERE c.event.id IN (?1) " +
            "GROUP BY c.event.id")
    List<ICommentCount> countTotalCommentsByEventIds(List<Integer> eventIds);
}
