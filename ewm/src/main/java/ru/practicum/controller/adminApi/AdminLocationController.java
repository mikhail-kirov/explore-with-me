package ru.practicum.controller.adminApi;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.LocationDto;
import ru.practicum.service.adminService.AdminLocationService;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/locations")
@RequiredArgsConstructor
@Slf4j
public class AdminLocationController {

    private final AdminLocationService adminLocationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationDto add(@RequestBody @Valid LocationDto locationDto) {
        log.info("Admin-запрос на добавление локации: {}", locationDto.getName());
        return adminLocationService.post(locationDto);
    }

    @GetMapping("/{locId}")
    public LocationDto getById(@PathVariable @Positive long locId) {
        log.info("Admin-запрос на получение локации по id = {}", locId);
        return adminLocationService.getLocationById(locId);
    }

    @PatchMapping("/{locId}")
    public LocationDto update(@PathVariable @Positive long locId,
                              @RequestBody @Valid LocationDto locationDto) {
        log.info("Admin-запрос на изменение локации по id = {}", locId);
        return adminLocationService.patchLocationById(locId, locationDto);
    }

    @GetMapping(("/all"))
    public List<LocationDto> getAllLocation() {
        log.info("Admin-запрос на получение списка всех локация");
        return adminLocationService.getAllLocations();
    }

    @DeleteMapping("/{locId}")
    public ResponseEntity<?> delete(@PathVariable long locId) {
        log.info("Admin-запрос на удаление локации");
        return adminLocationService.deleteLocation(locId);
    }
}
