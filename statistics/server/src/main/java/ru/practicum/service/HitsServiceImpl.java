package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exeption.BadRequestException;
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
import java.util.Collection;
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
    public Collection<ViewStatsDto> getStats(List<String> uris, String start, String end, boolean unique)  {
        LocalDateTime startDate = parseDate(start);
        LocalDateTime endDate = parseDate(end);
        validateAfterDate(startDate, endDate);
        Collection<ViewStatsDto> result;
            if (unique) {
                result = hitsRepository.getUniqueViewStats(uris, startDate, endDate);
            } else {
                result = hitsRepository.getViewStats(uris, startDate, endDate);
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

    private void validateAfterDate(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new BadRequestException("Значение стартовой даты поиска после конечной");
        }
    }
}
