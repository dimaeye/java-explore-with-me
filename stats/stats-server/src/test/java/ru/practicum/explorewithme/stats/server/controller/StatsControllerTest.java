package ru.practicum.explorewithme.stats.server.controller;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import ru.practicum.explorewithme.stats.client.HttpStatsClient;
import ru.practicum.explorewithme.stats.client.StatsClient;
import ru.practicum.explorewithme.stats.dto.HitDTO;
import ru.practicum.explorewithme.stats.dto.StatDTO;
import ru.practicum.explorewithme.stats.server.model.Hit;
import ru.practicum.explorewithme.stats.server.model.Stat;
import ru.practicum.explorewithme.stats.server.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StatsControllerTest {

    @MockBean
    private StatsService statsService;

    private final StatsClient statsClient;

    private final EasyRandom generator = new EasyRandom();

    StatsControllerTest(
            @Autowired
            RestTemplateBuilder restTemplateBuilder,
            @Value(value = "${local.server.port}")
            int port
    ) {
        this.statsClient = new HttpStatsClient(
                "http://localhost:" + port, restTemplateBuilder
        );
    }


    @Test
    void saveReq() {
        doNothing().when(statsService).saveReq(any(Hit.class));

        HitDTO hitDTO = HitDTO.builder()
                .app(generator.nextObject(String.class))
                .uri(generator.nextObject(String.class))
                .ip("127.0.0.1")
                .timestamp(LocalDateTime.now())
                .build();

        assertDoesNotThrow(
                () -> statsClient.saveReq(hitDTO)
        );
    }

    @Test
    void getStats() {
        List<Stat> stats = generator.objects(Stat.class, 10).collect(Collectors.toList());

        when(statsService.getStats(any(LocalDateTime.class), any(LocalDateTime.class), anyList(), anyBoolean()))
                .thenReturn(stats);

        List<StatDTO> statDTOS = statsClient.getStats(
                LocalDateTime.now(), LocalDateTime.now(), List.of("/events", "/users"), true
        );

        for (int i = 0; i < statDTOS.size(); i++) {
            assertEquals(stats.get(i).getApp(), statDTOS.get(i).getApp());
            assertEquals(stats.get(i).getUri(), statDTOS.get(i).getUri());
            assertEquals(stats.get(i).getHits(), statDTOS.get(i).getHits());
        }
    }
}