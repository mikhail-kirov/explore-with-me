package ru.practicum.service.publicService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.data.LocationRepository;
import ru.practicum.data.adminData.AdminEventsRepository;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.LocationDto;
import ru.practicum.mapper.MappingEvent;
import ru.practicum.mapper.MappingLocation;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.validation.ValidLocation;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PublicLocationServiceImpl implements PublicLocationService {

    private final LocationRepository locationRepository;
    private final AdminEventsRepository adminEventsRepository;
    private final ValidLocation validLocation;

    @Override
    public List<LocationDto> getLocationsByNameOrDescription(String text) {
        List<Location> locations = locationRepository.findByNameAndDescription(text);
        if (!locations.isEmpty()) {
            return MappingLocation.toLocationDto(locations);
        }
        return List.of();
    }

    @Override
    public List<LocationDto> getLocationsByCoordinatesAndRadius(float latitude, float longitude, float radius) {
        List<Location> locations = locationRepository.findByNearLocations(latitude, longitude, radius);
        if (!locations.isEmpty()) {
            return MappingLocation.toLocationDto(locations);
        }
        return List.of();
    }

    @Override
    public List<EventFullDto> getEventsByLocationId(long locationId) {
        Location location = validLocation.validateLocationById(locationId);
        List<Event> events = adminEventsRepository.findEventsByLocation(location);
        if (!events.isEmpty()) {
            return MappingEvent.toEventFullDto(events);
        }
        return List.of();
    }

    @Override
    public List<EventFullDto> getEventsByCoordinatesAndRadius(float latitude, float longitude, float radius) {
        List<Location> locations = locationRepository.findByNearLocations(latitude, longitude, radius);
        if (!locations.isEmpty()) {
            List<Event> events = adminEventsRepository.findAllByLocationIn(locations);
            if (!events.isEmpty()) {
                return MappingEvent.toEventFullDto(events);
            }
        }
        return List.of();
    }
}
