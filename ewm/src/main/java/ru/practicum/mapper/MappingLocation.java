package ru.practicum.mapper;

import ru.practicum.dto.LocationDto;
import ru.practicum.dto.LocationPrivateDto;
import ru.practicum.dto.LocationShortDto;
import ru.practicum.model.Location;

import java.util.List;

public class MappingLocation {

    public static Location toLocation(LocationDto locationDto) {
        return new Location(
                null,
                locationDto.getName(),
                locationDto.getDescription(),
                locationDto.getLat(),
                locationDto.getLon(),
                locationDto.getRadius(),
                locationDto.getOwner()
        );
    }

    public static Location toLocation(long userId, LocationDto locationDto) {
        return new Location(
                locationDto.getId() != null ? locationDto.getId() : null,
                locationDto.getName(),
                locationDto.getDescription(),
                locationDto.getLat(),
                locationDto.getLon(),
                locationDto.getRadius(),
                userId
        );
    }

    public static LocationDto toLocationDto(Location location) {
        return LocationDto.builder()
                .id(location.getId())
                .name(location.getName())
                .description(location.getDescription())
                .lat(location.getLat())
                .lon(location.getLon())
                .radius(location.getRadius())
                .owner(location.getOwner())
                .build();
    }

    public static List<LocationDto> toLocationDto(List<Location> locations) {
        return locations.stream().map(MappingLocation::toLocationDto).toList();
    }

    public static LocationPrivateDto toLocationPrivateDto(Location location) {
        return LocationPrivateDto.builder()
                .id(location.getId())
                .name(location.getName())
                .description(location.getDescription())
                .lat(location.getLat())
                .lon(location.getLon())
                .radius(location.getRadius())
                .build();
    }

    public static List<LocationPrivateDto> toLocationPrivateDto(List<Location> locations) {
        return locations.stream().map(MappingLocation::toLocationPrivateDto).toList();
    }

    public static LocationShortDto toLocationShortDto(Location location) {
        return new LocationShortDto(
                location.getLat(),
                location.getLon()
        );
    }

    public static Location toNewLocation(Location location, LocationDto locationDto) {
        location.setName(locationDto.getName() != null ? locationDto.getName() : location.getName());
        location.setDescription(locationDto.getDescription() != null ? locationDto.getDescription() : location.getDescription());
        location.setLat(locationDto.getLat() != null ? locationDto.getLat() : location.getLat());
        location.setLon(locationDto.getLon() != null ? locationDto.getLon() : location.getLon());
        location.setRadius(locationDto.getRadius() != null ? locationDto.getRadius() : location.getRadius());
        return location;
    }
}
