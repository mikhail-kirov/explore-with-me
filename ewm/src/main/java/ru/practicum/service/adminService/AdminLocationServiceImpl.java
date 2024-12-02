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
@Transactional
@RequiredArgsConstructor
public class AdminLocationServiceImpl implements AdminLocationService {

    private final LocationRepository locationRepository;
    private final ValidLocation validLocation;

    @Override
    public LocationDto post(LocationDto locationDto) {
        validLocation.validCreateCopyLocation(locationDto);
        Location location = MappingLocation.toLocation(locationDto);
        return MappingLocation.toLocationDto(locationRepository.save(location));
    }

    @Override
    public LocationDto getLocationById(long id) {
        Location location = validLocation.validateLocationById(id);
        return MappingLocation.toLocationDto(location);
    }

    @Override
    public LocationDto patchLocationById(long id, LocationDto locationDto) {
        Location location = validLocation.validateLocationById(id);
        MappingLocation.toNewLocation(location, locationDto);
        location = locationRepository.save(location);
        return MappingLocation.toLocationDto(location);
    }

    @Override
    public List<LocationDto> getAllLocations() {
        List<Location> locations = locationRepository.findAll();
        if (!locations.isEmpty()) {
            return MappingLocation.toLocationDto(locations);
        }
        return List.of();
    }

    @Override
    public ResponseEntity<Void> deleteLocation(long locationId) {
        validLocation.validateLocationById(locationId);
        validLocation.validExistEventsInLocation(locationId);
        locationRepository.deleteById(locationId);

        if (locationRepository.findById(locationId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
