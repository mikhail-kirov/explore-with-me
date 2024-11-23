package ru.practicum.controller.privateApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.service.privateService.PrivateParticipationRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestsController {

    private final PrivateParticipationRequestService privateParticipationRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable Long userId,
                                                 @RequestParam(required = false) Long eventId) {
        log.info("Private-запрос на участие в событии с ID: {}", eventId);
        return privateParticipationRequestService.postParticipationRequestDto(userId, eventId);
    }

    @GetMapping
    public List<ParticipationRequestDto> getParticipationRequest(@PathVariable Long userId) {
        log.info("Private-запрос на получение информации о заявке на участие от пользователя с ID: {}", userId);
        return privateParticipationRequestService.getParticipationRequestDto(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto patchParticipationRequest(@PathVariable Long userId,
                                                             @PathVariable Long requestId) {
        log.info("Private-запрос на отмену участия в событии. Запрос с ID: {}", requestId);
        return privateParticipationRequestService.patchCancelParticipationRequestDto(userId, requestId);
    }
}
