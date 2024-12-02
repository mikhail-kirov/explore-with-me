package ru.practicum.service.privateService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.data.LocationRepository;
import ru.practicum.data.privateData.PrivateEventRepository;
import ru.practicum.data.privateData.PrivateRequestRepository;
import ru.practicum.dto.*;
import ru.practicum.exeption.IncorrectParameterException;
import ru.practicum.mapper.MappingEvent;
import ru.practicum.mapper.MappingEventToNewEvent;
import ru.practicum.mapper.MappingRequest;
import ru.practicum.model.*;
import ru.practicum.validation.ValidCategory;
import ru.practicum.validation.ValidEvent;
import ru.practicum.validation.ValidLocation;
import ru.practicum.validation.ValidUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PrivateEventsServiceImpl implements PrivateEventsService {

    private final PrivateEventRepository privateEventRepository;
    private final PrivateRequestRepository privateRequestRepository;
    private final LocationRepository locationRepository;
    private final ValidUser validUser;
    private final ValidEvent validEvent;
    private final ValidCategory validCategory;
    private final ValidLocation validLocation;
    private final MappingEventToNewEvent mappingEventToNewEvent;

    @Override
    public EventFullDto postEvent(Long userId, NewEventDto newEvent) {

        validEvent.validNewEventByDate(newEvent.getEventDate());
        User user = validUser.validUserById(userId);
        Category category = validCategory.validCategoryById(newEvent.getCategory());
        Location location = validLocation.validateLocationById(newEvent.getLocation());
        validEvent.validNewEventDto(newEvent.getEventDate());

        Event event = MappingEvent.toEvent(user, category, newEvent, location);
        event = privateEventRepository.save(event);
        log.info("Событие сохранено: {}", event.getAnnotation());
        return MappingEvent.toEventFullDto(event);

    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsByInitiatorId(Long userId, Integer from, Integer size) {
        User user = validUser.validUserById(userId);
        List<Event> events = privateEventRepository.findByInitiatorId(userId, size, from);
        if (!events.isEmpty()) {
            log.info("Список событий пользователя {} отправлен", user.getEmail());
            return MappingEvent.toEventShortDto(events);
        }
        log.info("Список событий пользователя {} пуст", user.getEmail());
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventsByUserIdAndEventId(Long userId, Long eventId) {
        validUser.validUserById(userId);
        validEvent.validEventById(eventId);
        log.info("Событие {} пользователя {} отправлен", eventId, userId);
        return MappingEvent.toEventFullDto(privateEventRepository.findByInitiatorIdAndId(userId, eventId));
    }

    @Override
    public EventFullDto patchEventByUser(Long userId, Long eventId, EventPatchDto eventPatchDto) {
        validEvent.validNewEventByDate(eventPatchDto.getEventDate());
        validUser.validUserById(userId);
        Event event = validEvent.validPatchEventByState(eventId);
        validEvent.validNewEventDto(event.getEventDate());
        Location location = locationRepository.findById(eventPatchDto.getLocation()).orElse(null);
        Event newEvent = mappingEventToNewEvent.setNewEvent(event, eventPatchDto, location);
        privateEventRepository.save(newEvent);
        log.info("Событие '{}' изменено", newEvent.getAnnotation());
        return MappingEvent.toEventFullDto(newEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getParticipationRequest(Long userId, Long eventId) {
        validUser.validUserById(userId);
        validEvent.validEventById(eventId);
        log.info("Данные на участие пользователя {} на участие в событии {} отправлены", userId, eventId);
        List<Request> request = privateRequestRepository.findByEvent(eventId);
        if (request.isEmpty()) {
            return List.of();
        }
        return MappingRequest.toParticipationRequestDto(request);
    }

    @Override
    public EventRequestStatusUpdateResult patchEventRequestStatusUpdateRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest) {
        validUser.validUserById(userId);
        validEvent.validEventById(eventId);
        List<Long> eventReachedLimit = new ArrayList<>();
        List<Request> confirmedRequest = new ArrayList<>();
        List<Request> rejectedRequest = new ArrayList<>();

        List<Request> requests = privateRequestRepository.findAllByIdIn(updateRequest.getRequestIds());

        for (Request request : requests) {
            if (request.getStatus().equals(EventRequestStatus.CONFIRMED.toString())) {
                log.info("Ошибка: попытка отменить уже принятую заявку");
                throw new IncorrectParameterException("Attempt to cancel an already accepted application.", "Incorrectly made request.");
            }
            if (!request.getStatus().equals(EventRequestStatus.PENDING.toString())) {
                log.info("Ошибка. Запрос должен иметь статус 'PENDING'");
                throw new IncorrectParameterException("Request must have status PENDING.", "Incorrectly made request.");
            }
        }

        List<Event> events;
        if (updateRequest.getStatus().equals(EventRequestStatus.REJECTED)) {
            requests.forEach(req -> req.setStatus(EventRequestStatus.REJECTED.toString()));
            privateRequestRepository.saveAll(requests);
            rejectedRequest.addAll(requests);
        }

        if (updateRequest.getStatus().equals(EventRequestStatus.CONFIRMED)) {

            events = privateEventRepository.findAllByIdIn(requests.stream().map(Request::getEvent).toList());

            Map<Long, Request> requestsMap = requests.stream().collect(Collectors.toMap(Request::getEvent, req -> req));

            for (Event event : events) {
                if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
                    log.info("Ошибка. Достигнут лимит участников");
                    throw new IncorrectParameterException("The participant limit has been reached. Event: " + event.getAnnotation(),
                            "For the requested operation the conditions are not met.");
                }
            }

            requests.forEach(req -> req.setStatus(EventRequestStatus.CONFIRMED.toString()));

            for (Event event : events) {
                if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
                    confirmedRequest.add(requestsMap.get(event.getId()));
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                } else if (!event.getConfirmedRequests().equals(event.getParticipantLimit())) {
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                } else {
                    requests.remove(requestsMap.get(event.getId()));
                    eventReachedLimit.add(requestsMap.get(event.getId()).getId());
                }
                privateEventRepository.save(event);
            }
            privateRequestRepository.saveAll(requests);
            confirmedRequest.addAll(requests);
        }

        List<Request> canceledRequest = privateRequestRepository.findAllByIdIn(eventReachedLimit.stream().toList());

        if (!canceledRequest.isEmpty()) {
            canceledRequest.forEach(request -> request.setStatus(EventStatus.CANCELED.toString()));
            privateRequestRepository.saveAll(canceledRequest);
        }
        log.info("Статус на участие в событиях изменён");

        return new EventRequestStatusUpdateResult(MappingRequest.toParticipationRequestDto(confirmedRequest),
                MappingRequest.toParticipationRequestDto(rejectedRequest));
    }
}
