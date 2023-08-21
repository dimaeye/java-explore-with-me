package ru.practicum.explorewithme.stats.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.explorewithme.stats.dto.HitDTO;
import ru.practicum.explorewithme.stats.dto.StatDTO;
import ru.practicum.explorewithme.stats.server.mapper.HitMapper;
import ru.practicum.explorewithme.stats.server.mapper.StatMapper;
import ru.practicum.explorewithme.stats.server.model.Stat;
import ru.practicum.explorewithme.stats.server.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatsController {
    private final StatsService statsService;


    @PostMapping("/hit")
    public ResponseEntity<Void> saveReq(@Valid @RequestBody HitDTO hitDTO) {
        log.info("Запрос на сохранение информации {}", hitDTO);
        statsService.saveReq(HitMapper.toHit(hitDTO));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/stats")
    public List<StatDTO> getStats(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique
    ) {
        if (start.isAfter(end))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "start is after end");

        List<Stat> stats = statsService.getStats(start, end, uris, unique);

        return stats.stream().map(StatMapper::toStatDTO).collect(Collectors.toList());
    }
}
