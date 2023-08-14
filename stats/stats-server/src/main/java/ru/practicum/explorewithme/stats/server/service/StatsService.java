package ru.practicum.explorewithme.stats.server.service;

import ru.practicum.explorewithme.stats.server.model.Hit;
import ru.practicum.explorewithme.stats.server.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void saveReq(Hit hit);

    List<Stat> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
