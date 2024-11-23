package ru.practicum.controller.publicApi;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.RequestParamForPublicEvent;
import ru.practicum.service.publicService.PublicEventsService;

import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
public class PublicEventsController {

    private final PublicEventsService publicEventsService;

    @GetMapping
    public List<EventShortDto> getEventsWithFilters(@RequestParam(required = false) String text,
                                                    @RequestParam(required = false) List<Long> categories,
                                                    @RequestParam(required = false) Boolean paid,
                                                    @RequestParam(required = false) String rangeStart,
                                                    @RequestParam(required = false) String rangeEnd,
                                                    @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                                    @RequestParam(required = false) String sort,
                                                    @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                    @RequestParam(defaultValue = "10") @Positive int size,
                                                    HttpServletRequest httpServletRequest) {
        RequestParamForPublicEvent requestParam = RequestParamForPublicEvent.builder()
                        .text(text)
                        .categories(categories)
                        .paid(paid)
                        .rangeStart(rangeStart)
                        .rangeEnd(rangeEnd)
                        .onlyAvailable(onlyAvailable)
                        .sort(sort)
                        .from(from)
                        .size(size)
                        .build();
        log.info("Public-запрос на получение подборки событий");
        return publicEventsService.getEventsWithFilters(requestParam, httpServletRequest);
    }

    @GetMapping("/{id}")
    public EventFullDto getCompilationById(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        log.info("Public-запрос на получение информации о опубликованном событии c ID {}", id);
        return publicEventsService.getEventById(id, httpServletRequest);
    }
}
