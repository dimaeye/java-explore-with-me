package ru.practicum.explorewithme.stats.server.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.stats.dto.HitDTO;
import ru.practicum.explorewithme.stats.server.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class HitMapper {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Hit toHit(HitDTO hitDTO) {
        return Hit.builder()
                .app(hitDTO.getApp())
                .uri(hitDTO.getUri())
                .ip(hitDTO.getIp())
                .timestamp(LocalDateTime.parse(hitDTO.getTimestamp(), DATE_TIME_FORMATTER))
                .build();
    }
}
