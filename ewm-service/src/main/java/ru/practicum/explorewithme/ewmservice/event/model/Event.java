package ru.practicum.explorewithme.ewmservice.event.model;

import lombok.*;
import ru.practicum.explorewithme.ewmservice.category.model.Category;
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
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(length = 2000)
    private String annotation;
    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Category category;
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;
    @Column(length = 7000)
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @JoinColumn(name = "location_id")
    @OneToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Location location;
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    private String title;
    @JoinColumn(name = "initiator")
    @OneToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User initiator;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Enumerated(EnumType.STRING)
    private EventState state;
    private Integer views;
}
