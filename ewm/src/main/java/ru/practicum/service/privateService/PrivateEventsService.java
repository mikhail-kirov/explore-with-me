package ru.practicum.service.privateService;

import ru.practicum.dto.*;

import java.util.List;

public interface PrivateEventsService {

    EventFullDto postEvent(Long userId, NewEventDto event);

    List<EventShortDto> getEventsByInitiatorId(Long userId, Integer from, Integer size);

    EventFullDto getEventsByUserIdAndEventId(Long userId, Long eventId);

    EventFullDto patchEventByUser(Long userId, Long eventId, EventPatchDto event);

    List<ParticipationRequestDto> getParticipationRequest(Long userId, Long eventId);

    EventRequestStatusUpdateResult patchEventRequestStatusUpdateRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest);
}
