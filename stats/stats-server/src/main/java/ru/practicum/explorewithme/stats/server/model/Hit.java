package ru.practicum.explorewithme.stats.server.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hits")
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String app;

    private String uri;

    private String ip;

    @Column(name = "req_timestamp")
    private LocalDateTime timestamp;
}