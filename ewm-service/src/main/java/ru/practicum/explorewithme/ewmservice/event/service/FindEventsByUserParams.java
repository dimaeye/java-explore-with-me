package ru.practicum.explorewithme.ewmservice.event.service;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.explorewithme.ewmservice.event.model.Event;
import ru.practicum.explorewithme.ewmservice.event.model.SortParam;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class FindEventsByUserParams {
    private String text;
    private List<Integer> categories;
    private Boolean paid;
    private LocalDateTime startLocal;
    private LocalDateTime endLocal;
    private Boolean onlyAvailable;
    private SortParam sort;
    private int from;
    private int size = 10;

    Specification<Event> getFilteredEvents() {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (text != null)
                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("annotation")), "%" + text.toLowerCase() + "%"
                                ),
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("description")), "%" + text.toLowerCase() + "%"
                                )
                        )
                );

            if (categories != null)
                predicates.add(root.get("category").get("id").in(categories));

            if (paid != null)
                predicates.add(criteriaBuilder.equal(root.get("paid"), paid));

            if (startLocal != null)
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), startLocal));

            if (endLocal != null)
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), endLocal));

            if (onlyAvailable != null && onlyAvailable)
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.equal(root.get("participantLimit"), 0),
                        criteriaBuilder.greaterThan(root.get("participantLimit"), root.get("confirmedRequests")))
                );

            if (sort != null && sort.equals(SortParam.EVENT_DATE)) {
                query.orderBy(criteriaBuilder.desc(root.get("eventDate")));
            }
            if (sort != null && sort.equals(SortParam.VIEWS)) {
                query.orderBy(criteriaBuilder.desc(root.get("views")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
