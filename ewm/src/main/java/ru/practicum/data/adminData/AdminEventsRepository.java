package ru.practicum.data.adminData;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Event;
import ru.practicum.model.Location;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventsRepository extends JpaRepository<Event, Long> {

    @Query("select ev " +
            "from Event as ev " +
            "where ev.initiator.id in ?1 " +
            "and ev.state = ?2 " +
            "and ev.category.id in ?3 " +
            "and ev.eventDate between ?4 and ?5 " +
            "order by ev.createdOn asc " +
            "limit ?6 offset ?7")
    List<Event> getAllEventsByParameters(List<Long> users, String states, List<Long> categories,
                             LocalDateTime rangeStart, LocalDateTime rangeEnd, int size, int from);

    @Query("select ev from Event as ev")
    List<Event> findEventsByPage(Pageable pageable);

    List<Event> findEventsByLocation(Location location);

    List<Event> findAllByLocationIn(List<Location> locations);
}
