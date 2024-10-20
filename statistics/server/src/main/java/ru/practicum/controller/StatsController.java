package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.EndpointHitDto;
import ru.practicum.model.ViewStatsDto;
import ru.practicum.service.HitsService;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping()
@Slf4j
public class StatsController {

    private final HitsService hitsService;

    @PostMapping
    @RequestMapping("/hit")
    public EndpointHitDto createHit(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("Запрос на добавление статистики просмотра: {}, {}", endpointHitDto.getUri(), endpointHitDto.getIp());
        return hitsService.createHits(endpointHitDto);
    }

    @GetMapping()
    @RequestMapping("/stats")
    public Collection<ViewStatsDto> getStats(@RequestParam(value = "start") String start,
                                                             @RequestParam(value = "end") String end,
                                                             @RequestParam(value = "uris") List<String> uris,
                                                             @RequestParam(value = "unique", defaultValue = "false") boolean unique) {
        log.info("Запрос на получение статистики: {}", uris.stream().toList());
        return hitsService.getStats(uris, start, end, unique);
    }
}
