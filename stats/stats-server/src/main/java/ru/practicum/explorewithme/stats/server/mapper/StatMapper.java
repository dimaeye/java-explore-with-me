package ru.practicum.explorewithme.stats.server.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.stats.dto.StatDTO;
import ru.practicum.explorewithme.stats.server.model.Stat;

@UtilityClass
public class StatMapper {
    public static StatDTO toStatDTO(Stat stat) {
        return StatDTO.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .hits((int) stat.getHits())
                .build();
    }
}
