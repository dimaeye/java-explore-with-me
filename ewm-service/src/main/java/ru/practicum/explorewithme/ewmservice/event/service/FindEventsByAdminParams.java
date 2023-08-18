package ru.practicum.explorewithme.ewmservice.event.service;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.explorewithme.ewmservice.event.model.Event;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class FindEventsByAdminParams {
    private List<Integer> users;
    private List<String> states;
    private List<Integer> categories;
    private LocalDateTime startLocal;
    private LocalDateTime endLocal;
    private int from;
    private int size = 10;

    Specification<Event> getFilteredEvents() {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (users != null)
                predicates.add(root.get("initiator").get("id").in(users));

            if (states != null)
                predicates.add(root.get("state").in(states));

            if (categories != null)
                predicates.add(root.get("category").get("id").in(categories));

            if (startLocal != null)
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), startLocal));

            if (endLocal != null)
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), endLocal));


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
