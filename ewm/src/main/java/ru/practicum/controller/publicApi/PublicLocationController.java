package ru.practicum.controller.publicApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.LocationDto;
import ru.practicum.service.publicService.PublicLocationService;

import java.util.List;

@RestController
@RequestMapping(path = "/locations")
@RequiredArgsConstructor
@Slf4j
public class PublicLocationController {

    private final PublicLocationService publicLocationService;

    @GetMapping
    public List<LocationDto> getAllLocationByText(@RequestParam(value = "text") String text) {
        log.info("Public-запрос на получение выборки локаций с фильтрацией");
        return publicLocationService.getLocationsByNameOrDescription(text);
    }

    @GetMapping("/near")
    public List<LocationDto> getNearLocationsByCoordinatesAndRadius(@RequestParam (value = "lat") String latitude,
                                                                    @RequestParam (value = "lon") String longitude,
                                                                    @RequestParam (value = "rad") String radius) {
        log.info("Public-запрос на получение выборки ближайших локаций от указанной точки");
        return publicLocationService.getLocationsByCoordinatesAndRadius(Float.parseFloat(latitude), Float.parseFloat(longitude), Float.parseFloat(radius));
    }

    @GetMapping("/events/near")
    public List<EventFullDto> getNearEventsByCoordinatesAndRadius(@RequestParam (value = "lat") float latitude,
                                                                  @RequestParam (value = "lon") float longitude,
                                                                  @RequestParam (value = "rad") float radius) {
        log.info("Public-запрос на получение списка всех событий в заданных координатах и радиусу");
        return publicLocationService.getEventsByCoordinatesAndRadius(latitude, longitude, radius);
    }

    @GetMapping("/{locId}/events")
    public List<EventFullDto> getEventsByLocationId(@PathVariable long locId) {
        log.info("Public-запрос на получение списка событий заданной локации");
        return publicLocationService.getEventsByLocationId(locId);
    }
}
