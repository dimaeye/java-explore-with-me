package ru.practicum.explorewithme.ewmservice.compilation.model;

import lombok.*;
import ru.practicum.explorewithme.ewmservice.event.model.Event;

import javax.persistence.*;
import java.util.Set;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private Boolean pinned;
    @JoinTable(
            name = "compilations_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Event> events;
}
