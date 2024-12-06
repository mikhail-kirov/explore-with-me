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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PublicLocationServiceImpl implements PublicLocationService {

    private final LocationRepository locationRepository;
    private final AdminEventsRepository adminEventsRepository;
    private final ValidLocation validLocation;

    @Override
    public List<LocationDto> getLocationsByNameOrDescription(String text) {
        List<Location> locations = locationRepository.findByNameAndDescription(text);
        if (!locations.isEmpty()) {
            log.info("Список локаций с фильтром по имени и описанию в количестве {} отправлен", locations.size());
            return MappingLocation.toLocationDto(locations);
        }
        log.info("Локации не найдены");
        return List.of();
    }

    @Override
    public List<LocationDto> getLocationsByCoordinatesAndRadius(float latitude, float longitude, float radius) {
        List<Location> locations = locationRepository.findByNearLocations(latitude, longitude, radius);
        if (!locations.isEmpty()) {
            log.info("Список локаций с фильтром по координатам и радиусу в количестве {} отправлен", locations.size());
            return MappingLocation.toLocationDto(locations);
        }
        log.info("Локации не найдены");
        return List.of();
    }

    @Override
    public List<EventFullDto> getEventsByLocationId(long locationId) {
        Location location = validLocation.validateLocationById(locationId);
        List<Event> events = adminEventsRepository.findEventsByLocation(location);
        if (!events.isEmpty()) {
            log.info("Список событий по выбранной локации в количестве {} отправлен", events.size());
            return MappingEvent.toEventFullDto(events);
        }
        log.info("События не найдены");
        return List.of();
    }

    @Override
    public List<EventFullDto> getEventsByCoordinatesAndRadius(float latitude, float longitude, float radius) {
        List<Location> locations = locationRepository.findByNearLocations(latitude, longitude, radius);
        if (!locations.isEmpty()) {
            List<Event> events = adminEventsRepository.findAllByLocationIn(locations);
            if (!events.isEmpty()) {
                log.info("Список событий в пределах выбранных координат и радиуса в количестве {} отправлен", events.size());
                return MappingEvent.toEventFullDto(events);
            }
        }
        log.info("События не найдены");
        return List.of();
    }
}
