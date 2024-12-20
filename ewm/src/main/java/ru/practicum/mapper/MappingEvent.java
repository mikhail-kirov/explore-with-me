package ru.practicum.mapper;

import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.model.*;

import java.time.LocalDateTime;
import java.util.List;

public class MappingEvent {

    public static Event toEvent(User user, Category category, NewEventDto newEvent, Location location) {
        return new Event(
                null,
                newEvent.getAnnotation(),
                newEvent.getDescription(),
                newEvent.getTitle(),
                EventStatus.PENDING.toString(),
                LocalDateTime.now(),
                newEvent.getEventDate(),
                null,
                newEvent.getPaid(),
                newEvent.getRequestModeration(),
                newEvent.getParticipantLimit(),
                0L,
                0,
                location,
                user,
                category
        );
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(MappingCategory.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(MappingUser.toUserShortDto(event.getInitiator()))
                .location(MappingLocation.toLocationShortDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static List<EventFullDto> toEventFullDto(List<Event> events) {
        return events.stream().map(MappingEvent::toEventFullDto).toList();
    }

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(MappingCategory.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(MappingUser.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static List<EventShortDto> toEventShortDto(List<Event> events) {
        return events.stream().map(MappingEvent::toEventShortDto).toList();
    }
}
