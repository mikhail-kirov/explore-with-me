package ru.practicum.mapper;

import ru.practicum.model.EndpointHit;
import ru.practicum.model.EndpointHitDto;
import ru.practicum.model.ViewStatsDto;


public class MappingHits {

    public static EndpointHit mapToEndpointHit(EndpointHitDto hit) {
        return EndpointHit.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp())
                .build();
    }

    public static EndpointHitDto mapToEndpointHitDto(EndpointHit hit) {
        return EndpointHitDto.builder()
                .id(hit.getId())
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp())
                .build();
    }

    public static ViewStatsDto mapToStatsDtoFromHit(EndpointHit hit, int count) {
        return ViewStatsDto.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .hits(count)
                .build();
    }
}
