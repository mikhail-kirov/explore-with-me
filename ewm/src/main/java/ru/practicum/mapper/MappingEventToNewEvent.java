package ru.practicum.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.dto.EventPatchDto;
import ru.practicum.model.Event;
import ru.practicum.model.EventStatus;
import ru.practicum.model.Location;
import ru.practicum.validation.ValidCategory;

@Component
@AllArgsConstructor
public class MappingEventToNewEvent {

    private final ValidCategory validCategory;

    public Event setNewEvent(Event event, EventPatchDto eventPatch, Location location) {
        event.setAnnotation(eventPatch.getAnnotation() != null ? eventPatch.getAnnotation() : event.getAnnotation());
        event.setCategory(eventPatch.getCategory() != null ? validCategory.validCategoryById(eventPatch.getCategory()) : event.getCategory());
        event.setDescription(eventPatch.getDescription() != null ? eventPatch.getDescription() : event.getDescription());
        event.setEventDate(eventPatch.getEventDate() != null ? eventPatch.getEventDate() : event.getEventDate());
        event.setLocation(eventPatch.getLocation() != null ? location : event.getLocation());
        event.setPaid(eventPatch.getPaid() != null ? eventPatch.getPaid() : event.getPaid());
        event.setParticipantLimit(eventPatch.getParticipantLimit() != null ? eventPatch.getParticipantLimit() : event.getParticipantLimit());
        event.setRequestModeration(eventPatch.getRequestModeration() != null ? eventPatch.getRequestModeration() : event.getRequestModeration());
        event.setState(eventPatch.getStateAction() != null ? setEventStateByStateAction(eventPatch) : event.getState());
        event.setTitle(eventPatch.getTitle() != null ? eventPatch.getTitle() : event.getTitle());
        return event;
    }

    private String setEventStateByStateAction(EventPatchDto eventPatchDto) {
        return switch (eventPatchDto.getStateAction()) {
            case "REJECT_EVENT" -> EventStatus.REJECTED.toString();
            case "PUBLISH_EVENT" -> EventStatus.PUBLISHED.toString();
            case "CANCEL_REVIEW" -> EventStatus.CANCELED.toString();
            case "SEND_TO_REVIEW" -> EventStatus.PENDING.toString();
            default -> null;
        };
    }
}
