package ru.practicum.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select ht " +
           "from EndpointHit as ht " +
           "where ht.uri ilike %?1% " +
           "and ht.timestamp between ?2 and ?3")
    List<EndpointHit> findAllByDate(String uris, LocalDateTime start, LocalDateTime end);

    @Query("select ht " +
           "from EndpointHit as ht " +
           "where ht.uri ilike %?1% " +
           "and ht.timestamp between ?2 and ?3 " +
           "order by ht.ip")
    List<EndpointHit> findAllByDateAndUniqueIp(String uris, LocalDateTime start, LocalDateTime end);
}
