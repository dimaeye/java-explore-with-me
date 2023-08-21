package ru.practicum.explorewithme.ewmservice.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.ewmservice.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Page<User> findAllByIdIn(List<Integer> userIds, Pageable pageable);

    Optional<User> findByName(String name);
}
