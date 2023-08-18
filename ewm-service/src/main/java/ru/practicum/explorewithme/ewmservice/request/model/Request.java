package ru.practicum.explorewithme.ewmservice.request.model;

import lombok.*;
import ru.practicum.explorewithme.ewmservice.event.model.Event;
import ru.practicum.explorewithme.ewmservice.event.model.EventState;
import ru.practicum.explorewithme.ewmservice.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "event_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Event event;
    @JoinColumn(name = "requester_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User requester;
    private LocalDateTime created;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

}
