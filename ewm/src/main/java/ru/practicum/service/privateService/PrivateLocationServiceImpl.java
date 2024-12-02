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
import ru.practicum.model.User;
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
        User user = validUser.validUserById(userId);
        validLocation.validUserCountry(user, locationDto);
        Location location = MappingLocation.toLocation(userId, locationDto);
        return MappingLocation.toLocationPrivateDto(locationRepository.save(location));
    }

    @Override
    public LocationPrivateDto getLocationById(long userId, long locationDto) {
        validUser.validUserById(userId);
        validLocation.validOwnerLocation(locationDto, userId);
        Location location = validLocation.validateLocationById(locationDto);
        return MappingLocation.toLocationPrivateDto(location);
    }

    @Override
    public LocationPrivateDto patchLocationById(long userId, long locationId, LocationDto locationDto) {
        User user = validUser.validUserById(userId);
        validLocation.validOwnerLocation(locationId, userId);
        validLocation.validUserCountry(user, locationDto);
        Location location = validLocation.validateLocationById(locationId);
        MappingLocation.toNewLocation(location, locationDto);
        location = locationRepository.save(location);
        return MappingLocation.toLocationPrivateDto(location);
    }

    @Override
    public List<LocationPrivateDto> getAllMineLocations(long userId) {
        validUser.validUserById(userId);
        List<Location> locations = locationRepository.findAllByOwner(userId);
        if (!locations.isEmpty()) {
            return MappingLocation.toLocationPrivateDto(locations);
        }
        return List.of();
    }

    @Override
    public ResponseEntity<?> deleteLocation(long userId, long locationId) {
        validUser.validUserById(userId);
        validLocation.validExistEventsInLocation(locationId);
        locationRepository.deleteById(locationId);

        if (locationRepository.findById(locationId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
