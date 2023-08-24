package ru.practicum.explorewithme.ewmservice.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.ewmservice.comment.model.ICommentCount;
import ru.practicum.explorewithme.ewmservice.comment.repository.CommentRepository;
import ru.practicum.explorewithme.ewmservice.compilation.exception.CompilationNotFoundException;
import ru.practicum.explorewithme.ewmservice.compilation.model.Compilation;
import ru.practicum.explorewithme.ewmservice.compilation.repository.CompilationRepository;
import ru.practicum.explorewithme.ewmservice.event.model.Event;
import ru.practicum.explorewithme.ewmservice.event.repository.EventRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Compilation createCompilation(Compilation compilation) {
        if (compilation.getEvents() != null && !compilation.getEvents().isEmpty())
            compilation.setEvents(
                    new HashSet<>(
                            eventRepository.findAllByIdIn(compilation.getEvents().stream()
                                    .map(Event::getId)
                                    .collect(Collectors.toList()))
                    )
            );

        return compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void deleteCompilation(int compilationId) {
        Compilation compilation = findCompilation(compilationId);
        compilationRepository.delete(compilation);
    }

    @Override
    @Transactional
    public Compilation updateCompilation(int compilationId, Compilation compilation) {
        Compilation currentCompilation = findCompilation(compilationId);
        if (compilation.getTitle() != null)
            currentCompilation.setTitle(compilation.getTitle());
        if (compilation.getPinned() != null)
            currentCompilation.setPinned(compilation.getPinned());
        if (compilation.getEvents() != null && !compilation.getEvents().isEmpty()) {
            List<Integer> eventIds = compilation.getEvents().stream().map(Event::getId).collect(Collectors.toList());
            List<Event> events = eventRepository.findAllByIdIn(eventIds);
            Map<Integer, Long> commentsCount = getCommentsCount(eventIds);
            for (Event event : events)
                if (commentsCount.containsKey(event.getId()))
                    event.setCommentsCount(commentsCount.get(event.getId()));
            currentCompilation.setEvents(new HashSet<>(events));
        }

        return currentCompilation;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Compilation> getCompilations(boolean pinned, int from, int size) {
        List<Compilation> compilations =
                compilationRepository.findByPinned(pinned, PageRequest.of(from / size, size)).getContent();

        List<Integer> eventIds = compilations.stream()
                .flatMap(events -> events.getEvents().stream().map(Event::getId))
                .collect(Collectors.toList());
        Map<Integer, Long> commentsCount = getCommentsCount(eventIds);
        compilations.forEach(compilation ->
                compilation.getEvents().forEach(event -> {
                            if (commentsCount.containsKey(event.getId()))
                                event.setCommentsCount(commentsCount.get(event.getId()));
                        }
                )
        );

        return compilations;
    }

    @Override
    @Transactional(readOnly = true)
    public Compilation getCompilation(int compilationId) {
        Compilation compilation = findCompilation(compilationId);
        List<Integer> eventIds = compilation.getEvents().stream().map(Event::getId).collect(Collectors.toList());
        Map<Integer, Long> commentsCount = getCommentsCount(eventIds);
        for (Event event : compilation.getEvents())
            if (commentsCount.containsKey(event.getId()))
                event.setCommentsCount(commentsCount.get(event.getId()));

        return compilation;
    }

    private Compilation findCompilation(int compilationId) {
        return compilationRepository.findById(compilationId)
                .orElseThrow(() -> new CompilationNotFoundException(compilationId));
    }

    private Map<Integer, Long> getCommentsCount(List<Integer> eventIds) {
        return commentRepository
                .countTotalCommentsByEventIds(eventIds).stream()
                .collect(Collectors.toMap(ICommentCount::getEventId, ICommentCount::getTotalComment));
    }
}
