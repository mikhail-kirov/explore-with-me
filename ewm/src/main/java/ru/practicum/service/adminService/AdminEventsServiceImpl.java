package ru.practicum.service.adminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.data.adminData.AdminEventsRepository;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventPatchDto;
import ru.practicum.dto.RequestParamForAdminEvent;
import ru.practicum.exeption.IncorrectParameterException;
import ru.practicum.mapper.MappingEvent;
import ru.practicum.mapper.MappingEventToNewEvent;
import ru.practicum.model.Event;
import ru.practicum.model.EventStatus;
import ru.practicum.model.ParseDate;
import ru.practicum.model.StateAction;
import ru.practicum.validation.ValidEvent;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AdminEventsServiceImpl implements AdminEventsService {

    private final AdminEventsRepository adminEventsRepository;
    private final ValidEvent validEvent;
    private final ParseDate parseDate;
    private final MappingEventToNewEvent mappingEventToNewEvent;

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getEvents(RequestParamForAdminEvent requestParam) {
        List<Event> events;
        if (requestParam.getUsers() == null || requestParam.getUsers().isEmpty()) {
            events = adminEventsRepository.findEventsByPage(
                    PageRequest.of(requestParam.getFrom(), requestParam.getSize(), Sort.by(Sort.Direction.ASC, "createdOn"))
            );
        } else {
            events = adminEventsRepository.getAllEventsByParameters(
                    requestParam.getUsers(),
                    requestParam.getStates(),
                    requestParam.getCategories(),
                    parseDate.parse(requestParam.getRangeStart()),
                    parseDate.parse(requestParam.getRangeEnd()),
                    requestParam.getSize(),
                    requestParam.getFrom());
        }
        if(!events.isEmpty()) {
            log.info("Найдено и отправлено событий в количестве: {}", events.size());
            return events.stream().map(MappingEvent::toEventFullDto).toList();
        }
        return List.of();
    }

    @Override
    public EventFullDto patchAdminEvent(Long eventId, EventPatchDto eventPatchDto) {

        validEvent.validNewEventByDate(eventPatchDto.getEventDate());

        Event event = validEvent.validEventById(eventId);
        LocalDateTime now = LocalDateTime.now();

        if(!now.plusHours(1).isBefore(event.getEventDate())) {
            log.info("Изменения не допускаются менее чем за час до события");
            throw new IncorrectParameterException("Changes are not allowed less than an hour before the event",
                    "For the requested operation the conditions are not met.");
        }
        if(!event.getState().equals("PENDING")) {
            log.info("Невозможно опубликовать событие, так как оно находится в неправильном состоянии: {}", event.getState());
            throw new IncorrectParameterException("Cannot publish the event because it's not in the right state: " +  event.getState(),
                    "For the requested operation the conditions are not met.");
        }
        if(eventPatchDto.getStateAction() != null && eventPatchDto.getStateAction().equals(StateAction.REJECT_EVENT.toString()) &&
            event.getState().equals(EventStatus.PUBLISHED.toString())) {
            log.info("Не допускается отклонять опубликованные событий");
            throw new IncorrectParameterException("It is not allowed to reject published events",
                    "For the requested operation the conditions are not met.");
        }

        event = mappingEventToNewEvent.setNewEvent(event, eventPatchDto);

        if (event.getState().equals(EventStatus.PUBLISHED.name())) {
            event.setPublishedOn(now);
        }

        adminEventsRepository.save(event);
        log.info("Измененное событие сохранено");

        return MappingEvent.toEventFullDto(event);
    }
}
