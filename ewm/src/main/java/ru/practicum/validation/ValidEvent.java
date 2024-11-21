package ru.practicum.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.data.privateData.PrivateEventRepository;
import ru.practicum.exeption.BadRequestException;
import ru.practicum.exeption.IncorrectParameterException;
import ru.practicum.exeption.NotFoundException;
import ru.practicum.model.Event;
import ru.practicum.model.EventStatus;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class ValidEvent {

    private final PrivateEventRepository privateEventRepository;

    public void validNewEventDto(LocalDateTime eventDate) {
        if (LocalDateTime.now().plusHours(2).isAfter(eventDate)) {
            log.info("Поле: eventDate. Error: должно содержать дату, которая еще не наступила. Значение: {}", eventDate);
            throw new IncorrectParameterException("Field: eventDate. Error: must contain a date that has not yet occurred. Value: " + eventDate,
                    "For the requested operation the conditions are not met.");
        }
    }

    public Event validEventById(Long eventId) {
        if (eventId == null) {
            throw new BadRequestException("Event with id = null", "Bad request.");
        }
        return privateEventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found", "The required object was not found."));
    }

    public void validNewEventByDate(LocalDateTime eventDate) {
        if (eventDate != null && eventDate.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Даты события в прошлом.","Bad request.");
        }
    }

    public Event validPatchEventByState(Long eventId) {
        Event event = validEventById(eventId);
        if (event.getState().equals(EventStatus.PUBLISHED.toString())) {
            log.info("Изменить можно только запланированные или отмененные события.");
            throw new IncorrectParameterException("Only pending or canceled events can be changed",
                    "For the requested operation the conditions are not met.");
        }
        return event;
    }
}
