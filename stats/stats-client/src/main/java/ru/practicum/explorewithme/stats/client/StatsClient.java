package ru.practicum.explorewithme.stats.client;

import ru.practicum.explorewithme.stats.client.exception.StatsClientException;
import ru.practicum.explorewithme.stats.dto.HitDTO;
import ru.practicum.explorewithme.stats.dto.StatDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsClient {
    /**
     * @throws StatsClientException when failed to save hit
     */
    void saveReq(HitDTO hitDTO);

    /**
     * @throws StatsClientException when failed to get stats
     */
    List<StatDTO> getStats(
            LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique
    );
}
