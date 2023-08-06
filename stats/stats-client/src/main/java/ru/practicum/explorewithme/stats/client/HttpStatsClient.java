package ru.practicum.explorewithme.stats.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.explorewithme.stats.client.exception.StatsClientException;
import ru.practicum.explorewithme.stats.dto.HitDTO;
import ru.practicum.explorewithme.stats.dto.StatDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HttpStatsClient implements StatsClient {
    private final RestTemplate rest;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public HttpStatsClient(String statsServiceUrl, RestTemplateBuilder builder) {
        rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(statsServiceUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }


    @Override
    public void saveReq(HitDTO hitDTO) throws StatsClientException {
        HttpEntity<HitDTO> requestEntity = new HttpEntity<>(hitDTO);

        try {
            rest.exchange("/hit", HttpMethod.POST, requestEntity, Void.class);
        } catch (RestClientException e) {
            throw new StatsClientException(e.getMessage());
        }
    }

    @Override
    public List<StatDTO> getStats(
            LocalDateTime start, LocalDateTime end, @Nullable List<String> uris, Boolean unique
    ) throws StatsClientException {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("/stats")
                .queryParam("start", start.format(DATE_TIME_FORMATTER))
                .queryParam("end", end.format(DATE_TIME_FORMATTER))
                .queryParam("unique", unique);
        if (uris != null && !uris.isEmpty())
            uriBuilder.queryParam("uris", String.join(",", uris));

        String targetUrl = uriBuilder.build().toString();

        try {
            StatDTO[] response = rest.getForObject(targetUrl, StatDTO[].class);
            if (response != null)
                return Arrays.asList(response);
            else
                return Collections.emptyList();
        } catch (RestClientException e) {
            throw new StatsClientException(e.getMessage());
        }
    }
}
