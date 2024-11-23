package ru.practicum.data.publicData;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventsRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e " +
            "where e.description ilike (:text) " +
            "or e.annotation ilike (:text) " +
            "and e.category.id in (:categories) " +
            "and e.paid = (:paid) " +
            "and e.state = (:state) " +
            "and e.eventDate between :rangeStart AND :rangeEnd")
    List<Event> findEventsByFullParams(
            @Param("text") String text,
            @Param("categories") List<Long> categories,
            @Param("paid") Boolean paid,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            @Param("state") String state,
            Pageable pageable);

    @Query("select e from Event e " +
            "where e.description ilike (:text) " +
            "or e.annotation ilike (:text) " +
            "and e.category.id in (:categories) " +
            "and e.paid = (:paid) " +
            "and e.state = (:state) " +
            "and e.eventDate >= :rangeStart")
    List<Event> findEventsByShortParams(
            @Param("text") String text,
            @Param("categories") List<Long> categories,
            @Param("paid") Boolean paid,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("state") String state,
            Pageable pageable);

    @Query("select e from Event e " +
            "where e.category.id in (:categories) " +
            "and e.state = (:state)")
    List<Event> findEventsByPage(
            @Param("categories") List<Long> categories,
            @Param("state") String state,
            Pageable pageable);
}
