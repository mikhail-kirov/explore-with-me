package ru.practicum.controller.privateApi;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.LocationDto;
import ru.practicum.dto.LocationPrivateDto;
import ru.practicum.service.privateService.PrivateLocationService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/locations")
@RequiredArgsConstructor
@Slf4j
public class PrivateLocationController {

    private final PrivateLocationService privateLocationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationPrivateDto add(@PathVariable long userId,
                                  @RequestBody @Valid LocationDto locationDto) {
        log.info("Private-запрос на добавление локации: {}", locationDto.getName());
        return privateLocationService.post(userId, locationDto);
    }

    @GetMapping("/{locId}")
    public LocationPrivateDto getById(@PathVariable long userId,
                                      @PathVariable @Positive long locId) {
        log.info("Private-запрос на получение локации по id = {}", locId);
        return privateLocationService.getLocationById(userId, locId);
    }

    @PatchMapping("/{locId}")
    public LocationPrivateDto update(@PathVariable long userId,
                                     @PathVariable @Positive long locId,
                                     @RequestBody @Valid LocationDto locationDto) {
        log.info("Private-запрос на изменение локации по id = {}", locId);
        return privateLocationService.patchLocationById(userId, locId, locationDto);
    }

    @GetMapping(("/all"))
    public List<LocationPrivateDto> getAllLocation(@PathVariable long userId) {
        log.info("Private-запрос на получение списка всех локация");
        return privateLocationService.getAllMineLocations(userId);
    }

    @DeleteMapping("/{locId}")
    public ResponseEntity<?> delete(@PathVariable long userId, @PathVariable long locId) {
        log.info("Private-запрос на удаление локации");
        return privateLocationService.deleteLocation(userId, locId);
    }
}
