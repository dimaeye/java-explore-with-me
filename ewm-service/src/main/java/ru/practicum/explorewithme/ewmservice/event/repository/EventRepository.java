package ru.practicum.explorewithme.ewmservice.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.explorewithme.ewmservice.event.model.Event;
import ru.practicum.explorewithme.ewmservice.event.model.EventState;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer>, JpaSpecificationExecutor<Event> {
    Page<Event> findAll(Specification<Event> specification, Pageable pageable);

    Page<Event> findAllByInitiatorId(int userId, Pageable pageable);

    Optional<Event> findEventByIdAndInitiatorId(int eventId, int userId);

    Optional<Event> findEventByIdAndState(int eventId, EventState eventState);

    List<Event> findAllByIdIn(List<Integer> ids);

    boolean existsByCategoryId(int id);
}
