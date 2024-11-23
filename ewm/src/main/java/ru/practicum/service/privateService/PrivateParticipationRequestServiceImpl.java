package ru.practicum.service.privateService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.data.privateData.PrivateEventRepository;
import ru.practicum.data.privateData.PrivateRequestRepository;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.exeption.IncorrectParameterException;
import ru.practicum.mapper.MappingRequest;
import ru.practicum.model.Event;
import ru.practicum.model.EventRequestStatus;
import ru.practicum.model.EventStatus;
import ru.practicum.model.Request;
import ru.practicum.validation.ValidEvent;
import ru.practicum.validation.ValidRequest;
import ru.practicum.validation.ValidUser;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PrivateParticipationRequestServiceImpl implements PrivateParticipationRequestService {

    private final PrivateRequestRepository privateRequestRepository;
    private final PrivateEventRepository privateEventRepository;
    private final ValidUser validUser;
    private final ValidEvent validEvent;
    private final ValidRequest validRequest;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getParticipationRequestDto(Long userId) {
        List<Request> requests = privateRequestRepository.findAllByRequester(userId);
        log.info("Информация о заявке на участие в событии отправлена для пользователя: {}", userId);
        return MappingRequest.toParticipationRequestDto(requests);
    }

    @Override
    public ParticipationRequestDto postParticipationRequestDto(Long userId, Long eventId) {

        validUser.validUserById(userId);
        Event event = validEvent.validEventById(eventId);

        if (!event.getState().equals("PUBLISHED")) {
            throw new IncorrectParameterException("The event has not been published yet.", "Incorrectly made request.");
        }

        if (event.getParticipantLimit().equals(event.getConfirmedRequests()) && event.getParticipantLimit() != 0) {
            log.info("Ошибка: достигнут лимит участников");
            throw new IncorrectParameterException("Participant limit reached.", "Incorrectly made request.");
        }

        Request request;

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request = MappingRequest.toRequest(userId, eventId, EventRequestStatus.CONFIRMED.toString());
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            privateEventRepository.save(event);
            log.info("Запрос на участие в событии принят и подтвержден");
        } else {
            request = MappingRequest.toRequest(userId, eventId, EventRequestStatus.PENDING.toString());
        }

        List<Request> savedRequest = privateRequestRepository.findByRequesterAndEvent(userId, eventId);

        if (!savedRequest.isEmpty()) {
            log.info("Ошибка: попытка повторно подать заявку на то же мероприятие");
            throw new IncorrectParameterException("Trying to re-apply for the same event.", "Incorrectly made request.");
        }

        if (event.getInitiator().getId().equals(userId)) {
            log.info("Ошибка: инициатор запроса на участие и инициатор события совпадают");
            throw new IncorrectParameterException("Trying to leave a request to participate in your event.", "Incorrectly made request.");
        }

        try {
            request = privateRequestRepository.save(request);
        } catch (DataIntegrityViolationException e) {
            log.info("Ошибка сохранения заявки в БД");
            throw new IncorrectParameterException(e.getMessage(), "Integrity constraint has been violated.");
        }
        log.info("Запрос на участие в событии принят и ждет согласования");

        return MappingRequest.toParticipationRequestDto(request);
    }

    @Override
    public ParticipationRequestDto patchCancelParticipationRequestDto(Long userId, Long requestId) {

        validUser.validUserById(userId);
        Request request = validRequest.validateRequestById(requestId);

        if (!request.getRequester().equals(userId)) {
            throw new IncorrectParameterException("The request was initiated by another user.", "Incorrectly made request.");
        }
        request.setStatus(EventStatus.CANCELED.name());
        request = privateRequestRepository.save(request);
        log.info("Участие в событии отменено");

        Event event = validEvent.validEventById(request.getEvent());
        event.setConfirmedRequests(event.getConfirmedRequests() - 1);
        privateEventRepository.save(event);

        return MappingRequest.toParticipationRequestDto(request);
    }
}
