package ru.practicum.explorewithme.stats.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.stats.server.model.Hit;
import ru.practicum.explorewithme.stats.server.model.Stat;
import ru.practicum.explorewithme.stats.server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    @Transactional
    public void saveReq(Hit hit) {
        statsRepository.save(hit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stat> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (uris == null || uris.isEmpty()) {
            if (unique)
                return statsRepository.findAllUnique(start, end);
            else
                return statsRepository.findAll(start, end);
        } else {
            if (unique)
                return statsRepository.findAllUnique(start, end, uris);
            else
                return statsRepository.findAll(start, end, uris);
        }
    }
}
