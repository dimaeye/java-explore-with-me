package ru.practicum.explorewithme.stats.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithme.stats.client.exception.StatsClientException;
import ru.practicum.explorewithme.stats.dto.EndpointHitDTO;
import ru.practicum.explorewithme.stats.dto.ViewStatsDTO;

import java.time.LocalDateTime;
import java.util.List;

public class HttpStatsClient implements StatsClient {
    private final RestTemplate rest;

    public HttpStatsClient(String statsServiceUrl, RestTemplateBuilder builder) {
        rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(statsServiceUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }


    @Override
    public void saveReq(EndpointHitDTO endpointHitDTO) throws StatsClientException {
    }

    @Override
    public List<ViewStatsDTO> getStats(
            LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique
    ) throws StatsClientException {
        return null;
    }
}
