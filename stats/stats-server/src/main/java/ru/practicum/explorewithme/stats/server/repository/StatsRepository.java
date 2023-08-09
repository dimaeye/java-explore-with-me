package ru.practicum.explorewithme.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.stats.server.model.Hit;
import ru.practicum.explorewithme.stats.server.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Integer> {
    @Query("SELECT new ru.practicum.explorewithme.stats.server.model.Stat(" +
            "h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<Stat> findAllUnique(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.explorewithme.stats.server.model.Stat(" +
            "h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "AND h.uri IN(?3) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<Stat> findAllUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.explorewithme.stats.server.model.Stat(" +
            "h.app, h.uri, COUNT(h.ip)) " +
            "FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<Stat> findAll(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.explorewithme.stats.server.model.Stat(" +
            "h.app, h.uri, COUNT(h.ip)) " +
            "FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "AND h.uri IN(?3) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<Stat> findAll(LocalDateTime start, LocalDateTime end, List<String> uris);
}
