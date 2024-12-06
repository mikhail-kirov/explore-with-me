package ru.practicum.service.adminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.data.LocationRepository;
import ru.practicum.dto.LocationDto;
import ru.practicum.mapper.MappingLocation;
import ru.practicum.model.Location;
import ru.practicum.validation.ValidLocation;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminLocationServiceImpl implements AdminLocationService {

    private final LocationRepository locationRepository;
    private final ValidLocation validLocation;

    @Override
    @Transactional
    public LocationDto post(LocationDto locationDto) {
        validLocation.validCreateCopyLocation(locationDto);
        Location location = MappingLocation.toLocation(locationDto);
        log.info("Admin-локация '{}' сохранена", location.getName());
        return MappingLocation.toLocationDto(locationRepository.save(location));
    }

    @Override
    public LocationDto getLocationById(long id) {
        Location location = validLocation.validateLocationById(id);
        log.info("Admin. Локация c ID {} найдена и отправлена", id);
        return MappingLocation.toLocationDto(location);
    }

    @Override
    @Transactional
    public LocationDto patchLocationById(long id, LocationDto locationDto) {
        Location location = validLocation.validateLocationById(id);
        MappingLocation.toNewLocation(location, locationDto);
        location = locationRepository.save(location);
        log.info("Admin. Изменения сохранены");
        return MappingLocation.toLocationDto(location);
    }

    @Override
    public List<LocationDto> getAllLocations() {
        List<Location> locations = locationRepository.findAll();
        if (!locations.isEmpty()) {
            log.info("Admin. Список локаций в количестве {} отправлен", locations.size());
            return MappingLocation.toLocationDto(locations);
        }
        log.info("Admin. Локации не найдены");
        return List.of();
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteLocation(long locationId) {
        validLocation.validateLocationById(locationId);
        validLocation.validExistEventsInLocation(locationId);
        locationRepository.deleteById(locationId);

        if (locationRepository.findById(locationId).isEmpty()) {
            log.info("Admin. Локации с ID {} удалена", locationId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        log.info("Admin. Ошибка. Локация с ID {} не удалена", locationId);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
