package ru.practicum.explorewithme.stats.server.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.stats.dto.HitDTO;
import ru.practicum.explorewithme.stats.server.model.Hit;

@UtilityClass
public class HitMapper {

    public static Hit toHit(HitDTO hitDTO) {
        return Hit.builder()
                .app(hitDTO.getApp())
                .uri(hitDTO.getUri())
                .ip(hitDTO.getIp())
                .timestamp(hitDTO.getTimestamp())
                .build();
    }
}
