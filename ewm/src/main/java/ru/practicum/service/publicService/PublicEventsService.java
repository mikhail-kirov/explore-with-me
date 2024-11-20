package ru.practicum.service.publicService;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.RequestParamForPublicEvent;

import java.util.List;

public interface PublicEventsService {
    List<EventShortDto> getEventsWithFilters(RequestParamForPublicEvent requestParam, HttpServletRequest request);
    EventFullDto getEventById(long eventId, HttpServletRequest request);
}
