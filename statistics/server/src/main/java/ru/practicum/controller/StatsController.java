package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.EndpointHitDto;
import ru.practicum.model.ViewStatsDto;
import ru.practicum.service.HitsService;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final HitsService hitsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto createHit(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("Запрос на добавление статистики просмотра: {}, {}", endpointHitDto.getUri(), endpointHitDto.getIp());
        return hitsService.createHits(endpointHitDto);
    }

    @GetMapping("/stats")
    public Collection<ViewStatsDto> getStats(@RequestParam(value = "start") String start,
                                             @RequestParam(value = "end") String end,
                                             @RequestParam(required = false, value = "uris") List<String> uris,
                                             @RequestParam(value = "unique", defaultValue = "false") boolean unique) {
        log.info("Запрос на получение статистики: {}", uris);
        return hitsService.getStats(uris, start, end, unique);
    }

    @GetMapping("/stats/ip")
    public Boolean existIp(@RequestParam(value = "requestIp") String ip,
                           @RequestParam(value = "uri") String uri) {
        log.info("Запрос на поиск ip: {}", ip);
        return hitsService.existByIpAndUri(ip, uri);
    }
}
