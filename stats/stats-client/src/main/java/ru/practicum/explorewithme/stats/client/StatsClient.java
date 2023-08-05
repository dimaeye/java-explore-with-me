package ru.practicum.explorewithme.stats.client;

import ru.practicum.explorewithme.stats.client.exception.StatsClientException;
import ru.practicum.explorewithme.stats.dto.EndpointHitDTO;
import ru.practicum.explorewithme.stats.dto.ViewStatsDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsClient {
    void saveReq(EndpointHitDTO endpointHitDTO) throws StatsClientException;

    List<ViewStatsDTO> getStats(
            LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique
    ) throws StatsClientException;
}
