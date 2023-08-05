package ru.practicum.explorewithme.stats.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Stat {
    private String app;
    private String uri;
    private long hits;
}