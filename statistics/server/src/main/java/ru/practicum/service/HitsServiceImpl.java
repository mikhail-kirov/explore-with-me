package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exeption.IncorrectParameterException;
import ru.practicum.mapper.MappingHits;
import ru.practicum.model.EndpointHitDto;
import ru.practicum.data.HitsRepository;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStatsDto;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HitsServiceImpl implements HitsService {

    private final HitsRepository hitsRepository;

    @Override
    public EndpointHitDto createHits(EndpointHitDto hit) {
        EndpointHit endpointHit = hitsRepository.save(MappingHits.mapToEndpointHit(hit));
        log.info("Сохранено для: {}, {}", endpointHit.getUri(), endpointHit.getIp());
        return MappingHits.mapToEndpointHitDto(endpointHit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewStatsDto> getStats(List<String> uris, String start, String end, boolean unique)  {
        List<ViewStatsDto> result = new ArrayList<>();
        List<EndpointHit> hitBd;
        if (unique) {
            for (String uri : uris) {
                hitBd = hitsRepository.findAllByDateAndUniqueIp(uri, parseDate(start), parseDate(end));
                if (!hitBd.isEmpty()) {
                int count = 1;
                EndpointHit test = hitBd.getFirst();
                for (EndpointHit hit : hitBd) {
                    if (!hit.getIp().equals(test.getIp())) {
                        count++;
                    }
                    test = hit;
                }
                    result.add(MappingHits.mapToStatsDtoFromHit(hitBd.getFirst(), count));
                }
            }
            log.info("Статистика отправлена. Количество найденных ресурсов: {}", result.size());
            return result;
        }
        for (String uri : uris) {
            hitBd = hitsRepository.findAllByDate(uri, parseDate(start), parseDate(end));
            if (!hitBd.isEmpty()) {
                result.add(MappingHits.mapToStatsDtoFromHit(hitBd.getFirst(), hitBd.size()));
            }
        }
            log.info("Статистика отправлена. Количество найденных ресурсов: {}", result.size());
            return result;
    }

    private LocalDateTime parseDate(String date) {
        String decode = URLDecoder.decode(date, StandardCharsets.UTF_8);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            return LocalDateTime.parse(decode, formatter);
        } catch (DateTimeParseException e) {
            log.info("Дата {} передана в неверном формате", decode);
            throw new IncorrectParameterException("Дата " + decode + " передана в неверном формате");
        }
    }
}
