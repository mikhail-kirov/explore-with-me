package ru.practicum.service.publicService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.data.publicData.PublicCategoriesRepository;
import ru.practicum.data.publicData.PublicEventsRepository;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.RequestParamForPublicEvent;
import ru.practicum.exeption.BadRequestException;
import ru.practicum.exeption.NotFoundException;
import ru.practicum.mapper.MappingEvent;
import ru.practicum.model.*;
import ru.practicum.service.StatsService;
import ru.practicum.validation.ValidEvent;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PublicEventsServiceImpl implements PublicEventsService {

    private final PublicEventsRepository publicEventsRepository;
    private final PublicCategoriesRepository publicCategoriesRepository;
    private final StatsService statsService;
    private final ValidEvent validEvent;
    private final ParseDate parseDate;

    @Value("${ewm.service.name}")
    private String serviceName;

    @Override
    public List<EventShortDto> getEventsWithFilters(RequestParamForPublicEvent requestParam, HttpServletRequest request) {
        boolean isCategory = publicCategoriesRepository.existsByIdIn(requestParam.getCategories());
        if (!isCategory) {
            throw new BadRequestException("Category not found", "Bad request");
        }
        List<Event> events;
        if (requestParam.getText() == null || requestParam.getText().isEmpty()) {
            events = publicEventsRepository.findEventsByPage(
                    requestParam.getCategories(),
                    "PUBLISHED",
                    createPageRequest(requestParam.getSort(), requestParam.getFrom(), requestParam.getSize())
            );
        } else {
            if (requestParam.getRangeEnd() == null) {
                events = publicEventsRepository.findEventsByShortParams(
                        requestParam.getText(),
                        requestParam.getCategories(),
                        requestParam.getPaid(),
                        parseDate.parse(requestParam.getRangeStart()),
                        "PUBLISHED",
                        createPageRequest(requestParam.getSort(), requestParam.getFrom(), requestParam.getSize())
                );
            } else {
                events = publicEventsRepository.findEventsByFullParams(
                        requestParam.getText(),
                        requestParam.getCategories(),
                        requestParam.getPaid(),
                        parseDate.parse(requestParam.getRangeStart()),
                        parseDate.parse(requestParam.getRangeEnd()),
                        "PUBLISHED",
                        createPageRequest(requestParam.getSort(), requestParam.getFrom(), requestParam.getSize())
                );
            }
        }
        if (!events.isEmpty()) {
            if (!getExistIp(request)) {
                events.forEach(event -> event.setViews(event.getViews() + 1));
                publicEventsRepository.saveAll(events);
            }
            saveStats(request);
            log.info("Информация о запросе отправлена сервису статистики");
            log.info("Публичный запрос на получение подборки событий обработан, данные отправлены");
            return events.stream().map(MappingEvent::toEventShortDto).toList();
        }
        return List.of();
    }

    @Override
    @Transactional
    public EventFullDto getEventById(long eventId, HttpServletRequest request) {
        Event event = validEvent.validEventById(eventId);
        if (!event.getState().equals(EventStatus.PUBLISHED.toString())) {
            log.info("Событие с id={} не опубликовано", eventId);
            throw new NotFoundException("Event with id=" + eventId + " is not published", "Incorrectly made request.");
        }
        if (!getExistIp(request)) {
            event.setViews(event.getViews() + 1);
            publicEventsRepository.save(event);
        }
        saveStats(request);
        log.info("Информация о запросе отправлена на сервис статистики");
        log.info("Информации о событии с ID {} отправлена", eventId);
        return MappingEvent.toEventFullDto(event);
    }

    private PageRequest createPageRequest(String sort, int from, int size) {
        if (sort == null || sort.equalsIgnoreCase("EVENT_DATE")) {
            return PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "eventDate"));
        }
        if (sort.equalsIgnoreCase("VIEWS")) {
            return PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "views"));
        }
        return null;
    }

    private void saveStats(HttpServletRequest request) {
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app(serviceName)
                .timestamp(LocalDateTime.now())
                .build();
        statsService.createHit(endpointHitDto);
    }

    private Boolean getExistIp(HttpServletRequest request) {
        return (Boolean) statsService.getExistByIpAndUri(request.getRemoteAddr(), request.getRequestURI()).getBody();
    }
}
