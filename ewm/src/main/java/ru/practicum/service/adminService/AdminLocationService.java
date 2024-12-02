package ru.practicum.service.adminService;

import org.springframework.http.ResponseEntity;
import ru.practicum.dto.LocationDto;

import java.util.List;

public interface AdminLocationService {

    LocationDto post(LocationDto locationDto);

    LocationDto getLocationById(long id);

    LocationDto patchLocationById(long id, LocationDto locationDto);

    List<LocationDto> getAllLocations();

    ResponseEntity<?> deleteLocation(long locationId);
}
