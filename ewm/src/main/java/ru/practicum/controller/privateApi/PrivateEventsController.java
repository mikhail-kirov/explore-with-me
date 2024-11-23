package ru.practicum.controller.privateApi;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.*;
import ru.practicum.service.privateService.PrivateEventsService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class PrivateEventsController {

    private final PrivateEventsService privateEventsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @RequestBody @Valid NewEventDto newEventDto) {
        log.info("Private-запрос на добавление категории: {}", newEventDto.getAnnotation());
        return privateEventsService.postEvent(userId, newEventDto);
    }

    @GetMapping
    public List<EventShortDto> getEventByUserId(@PathVariable Long userId,
                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Private-запрос на получение списка событий пользователя с ID: {}", userId);
        return privateEventsService.getEventsByInitiatorId(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventsByUserIdAndEventId(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Private-запрос на получение события пользователя с ID: {}", userId);
        return privateEventsService.getEventsByUserIdAndEventId(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                   @RequestBody @Valid EventPatchDto eventPatchDto) {
        log.info("Private-запрос на изменение события с ID: {}", eventId);
        return privateEventsService.patchEventByUser(userId, eventId, eventPatchDto);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequest(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Private-запрос на получение статуса запроса на участие от пользователя с ID: {}", userId);
        return privateEventsService.getParticipationRequest(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult patchEventRequestStatusUpdate(@PathVariable Long userId, @PathVariable Long eventId,
                                   @RequestBody @Valid EventRequestStatusUpdateRequest updateRequest) {
        log.info("Private-запрос на изменение статуса заявок: {}", updateRequest.getRequestIds());
        return privateEventsService.patchEventRequestStatusUpdateRequest(userId, eventId, updateRequest);
    }

}
