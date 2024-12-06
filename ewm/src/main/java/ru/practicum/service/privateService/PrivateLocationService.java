package ru.practicum.service.privateService;

import org.springframework.http.ResponseEntity;
import ru.practicum.dto.LocationDto;
import ru.practicum.dto.LocationPrivateDto;

import java.util.List;

public interface PrivateLocationService {

    LocationPrivateDto post(long userId, LocationDto locationDto);

    LocationPrivateDto getLocationById(long userId, long colId);

    LocationPrivateDto patchLocationById(long userId, long locationId, LocationDto locationDto);

    List<LocationPrivateDto> getAllMineLocations(long userId);

    ResponseEntity<?> deleteLocation(long userId, long locationId);
}
