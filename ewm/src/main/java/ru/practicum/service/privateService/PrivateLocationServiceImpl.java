package ru.practicum.service.privateService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.data.LocationRepository;
import ru.practicum.dto.LocationDto;
import ru.practicum.dto.LocationPrivateDto;
import ru.practicum.mapper.MappingLocation;
import ru.practicum.model.Location;
import ru.practicum.validation.ValidLocation;
import ru.practicum.validation.ValidUser;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PrivateLocationServiceImpl implements PrivateLocationService {

    private final LocationRepository locationRepository;
    private final ValidLocation validLocation;
    private final ValidUser validUser;

    @Override
    public LocationPrivateDto post(long userId, LocationDto locationDto) {
        validLocation.validCreateCopyLocation(locationDto);
        validUser.validUserById(userId);
        validLocation.validUserCountry(locationDto);
        Location location = MappingLocation.toLocation(userId, locationDto);
        log.info("User-локация '{}' сохранена", location.getName());
        return MappingLocation.toLocationPrivateDto(locationRepository.save(location));
    }

    @Override
    @Transactional(readOnly = true)
    public LocationPrivateDto getLocationById(long userId, long locationDto) {
        validUser.validUserById(userId);
        validLocation.validOwnerLocation(locationDto, userId);
        Location location = validLocation.validateLocationById(locationDto);
        log.info("User c ID {}. Локация '{}' найдена и отправлена", userId, location.getName());
        return MappingLocation.toLocationPrivateDto(location);
    }

    @Override
    public LocationPrivateDto patchLocationById(long userId, long locationId, LocationDto locationDto) {
        validUser.validUserById(userId);
        validLocation.validOwnerLocation(locationId, userId);
        validLocation.validUserCountry(locationDto);
        Location location = validLocation.validateLocationById(locationId);
        MappingLocation.toNewLocation(location, locationDto);
        location = locationRepository.save(location);
        log.info("User c ID {}. Данные локации '{}' обновлены", userId, location.getName());
        return MappingLocation.toLocationPrivateDto(location);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationPrivateDto> getAllMineLocations(long userId) {
        validUser.validUserById(userId);
        List<Location> locations = locationRepository.findAllByOwner(userId);
        if (!locations.isEmpty()) {
            log.info("User c ID {}. Список локаций в количестве {} отправлен", userId, locations.size());
            return MappingLocation.toLocationPrivateDto(locations);
        }
        log.info("User c ID {}. Локации не найдены", userId);
        return List.of();
    }

    @Override
    public ResponseEntity<?> deleteLocation(long userId, long locationId) {
        validUser.validUserById(userId);
        validLocation.validExistEventsInLocation(locationId);
        locationRepository.deleteById(locationId);

        if (locationRepository.findById(locationId).isEmpty()) {
            log.info("User c ID {}. Локации с ID {} удалена", userId, locationId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        log.info("User c ID {}. Ошибка. Локация с ID {} не удалена", userId, locationId);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
