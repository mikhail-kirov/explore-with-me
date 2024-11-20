package ru.practicum.service.privateService;

import ru.practicum.dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateParticipationRequestService {

    List<ParticipationRequestDto> getParticipationRequestDto(Long userId);

    ParticipationRequestDto postParticipationRequestDto(Long userId, Long eventId);

    ParticipationRequestDto patchCancelParticipationRequestDto(Long userId, Long requestId);
}
