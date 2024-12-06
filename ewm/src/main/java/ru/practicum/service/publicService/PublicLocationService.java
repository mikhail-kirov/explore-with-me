package ru.practicum.service.publicService;

import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.LocationDto;

import java.util.List;

public interface PublicLocationService {

    List<LocationDto> getLocationsByNameOrDescription(String nameOrDescription);

    List<LocationDto> getLocationsByCoordinatesAndRadius(float latitude, float longitude, float radius);

    List<EventFullDto> getEventsByLocationId(long locationId);

    List<EventFullDto> getEventsByCoordinatesAndRadius(float latitude, float longitude, float radius);
}
