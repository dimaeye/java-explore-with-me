package ru.practicum.explorewithme.ewmservice.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.ewmservice.compilation.exception.CompilationNotFoundException;
import ru.practicum.explorewithme.ewmservice.compilation.model.Compilation;
import ru.practicum.explorewithme.ewmservice.compilation.repository.CompilationRepository;
import ru.practicum.explorewithme.ewmservice.event.model.Event;
import ru.practicum.explorewithme.ewmservice.event.repository.EventRepository;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public Compilation createCompilation(Compilation compilation) {
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
            List<Event> events = eventRepository.findAllByIdIn(
                    compilation.getEvents().stream().map(Event::getId).collect(Collectors.toList())
            );
            currentCompilation.setEvents(new HashSet<>(events));
        }

        return compilationRepository.save(currentCompilation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Compilation> getCompilations(boolean pinned, int from, int size) {
        return compilationRepository.findByPinned(pinned, PageRequest.of(from / size, size)).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public Compilation getCompilation(int compilationId) {
        return findCompilation(compilationId);
    }

    private Compilation findCompilation(int compilationId) {
        return compilationRepository.findById(compilationId)
                .orElseThrow(() -> new CompilationNotFoundException(compilationId));
    }
}
