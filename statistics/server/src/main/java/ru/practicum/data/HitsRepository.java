package ru.practicum.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select new ru.practicum.model.ViewStatsDto(ht.app, ht.uri, count(ht.ip)) " +
            "from EndpointHit as ht " +
            "where ht.timestamp between ?2 and ?3 " +
            "and (ht.uri in (?1) or (?1) is null) " +
            "group by ht.uri, ht.app " +
            "order by count(ht.ip) desc")
    List<ViewStatsDto> getViewStats(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.model.ViewStatsDto(ht.app, ht.uri, count(distinct ht.ip)) " +
            "from EndpointHit as ht " +
            "where ht.timestamp between ?2 and ?3 " +
            "and (ht.uri in (?1) or (?1) is null) " +
            "group by ht.uri, ht.app " +
            "order by count(distinct ht.ip) desc")
    List<ViewStatsDto> getUniqueViewStats(List<String> uris, LocalDateTime start, LocalDateTime end);
}
