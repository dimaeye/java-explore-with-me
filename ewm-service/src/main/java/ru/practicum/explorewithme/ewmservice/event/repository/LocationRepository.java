package ru.practicum.explorewithme.ewmservice.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.ewmservice.event.model.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    Optional<Location> findByLatAndLon(float lat, float lon);
}
