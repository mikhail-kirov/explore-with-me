package ru.practicum.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.data.LocationRepository;
import ru.practicum.data.adminData.AdminEventsRepository;
import ru.practicum.dto.LocationDto;
import ru.practicum.exeption.BadRequestException;
import ru.practicum.exeption.IncorrectParameterException;
import ru.practicum.exeption.NotFoundException;
import ru.practicum.model.Location;
import ru.practicum.model.User;
import ru.practicum.service.ExternalService;

@Component
@Slf4j
@RequiredArgsConstructor
public class ValidLocation {

    private final LocationRepository locationRepository;
    private final AdminEventsRepository adminEventsRepository;
    private final ExternalService externalService;

    public Location validateLocationById(Long id) {
        if (id == null) {
            throw new BadRequestException("Location with id = null", "Bad request.");
        }
        return locationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Location with id=" + id + " was not found", "The Location object was not found."));
    }

    public void validOwnerLocation(long locationId, long ownerId) {
        Location location = validateLocationById(locationId);
        if (location.getOwner() != ownerId) {
            throw new IncorrectParameterException("Запрос операции с чужой локацией. Отказано.", "Incorrect Parameter");
        }
    }

    public void validCreateCopyLocation(LocationDto locationDto) {
        Location locationBd = locationRepository.findByNameAndDescriptionAndLatAndLon(
                locationDto.getName(),
                locationDto.getDescription(),
                locationDto.getLat(),
                locationDto.getLon()
        );
        if (locationBd != null) {
            throw new IncorrectParameterException("Локация с переданными параметрами уже создана. Отказано.", "Incorrect Parameter");
        }
    }

    public void validUserCountry(User user, LocationDto locationDto) {
        if (user.getCountry() == null) {
            throw new BadRequestException("Нельзя создать локацию пользователю без указанной страны в профиле", "Bad Request");
        }
        if (!externalService.validateCoordinatesByCountry(locationDto.getLat(), locationDto.getLon(), user.getCountry())) {
            throw new BadRequestException("Страна указанная в профиле пользователя не совпадает со страной переданных координат. Отказано.", "Bad Request");
        }
    }

    public void validExistEventsInLocation(long locationId) {
        Location location = validateLocationById(locationId);
        if (!adminEventsRepository.findEventsByLocation(location).isEmpty()) {
            throw new BadRequestException("Для локации созданы событие(я). Нельзя удалить.", "Bad Request.");
        }
    }
}
