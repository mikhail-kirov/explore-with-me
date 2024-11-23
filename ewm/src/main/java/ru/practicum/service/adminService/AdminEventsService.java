package ru.practicum.service.adminService;

import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventPatchDto;
import ru.practicum.dto.RequestParamForAdminEvent;

import java.util.List;

public interface AdminEventsService {

    List<EventFullDto> getEvents(RequestParamForAdminEvent requestParamForAdminEvent);

    EventFullDto patchAdminEvent(Long eventId, EventPatchDto eventPatchDto);
}
