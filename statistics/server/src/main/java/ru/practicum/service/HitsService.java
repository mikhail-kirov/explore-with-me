package ru.practicum.service;

import ru.practicum.model.EndpointHitDto;
import ru.practicum.model.ViewStatsDto;

import java.util.Collection;
import java.util.List;

public interface HitsService {

    EndpointHitDto createHits(EndpointHitDto hit);

    Collection<ViewStatsDto> getStats(List<String> uris, String start, String end, boolean unique);

    Boolean existByIpAndUri(String ip, String uri);
}
