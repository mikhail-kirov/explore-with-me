package ru.practicum.mapper;

import ru.practicum.dto.*;
import ru.practicum.model.*;

import java.time.LocalDateTime;
import java.util.List;

public class MappingRequest {

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .created(request.getCreated())
                .event(request.getEvent())
                .id(request.getId())
                .requester(request.getRequester())
                .status(request.getStatus())
                .build();
    }

    public static Request toRequest(Long userId, Long eventId, String requestStatus) {
        return new Request(
                LocalDateTime.now(),
                eventId,
                null,
                userId,
                requestStatus
        );
    }

    public static List<ParticipationRequestDto> toParticipationRequestDto(List<Request> requests) {
        return requests.stream().map(MappingRequest::toParticipationRequestDto).toList();
    }
}
