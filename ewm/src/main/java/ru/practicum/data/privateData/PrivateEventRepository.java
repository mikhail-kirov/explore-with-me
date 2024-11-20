package ru.practicum.data.privateData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Category;
import ru.practicum.model.Event;

import java.util.List;

public interface PrivateEventRepository extends JpaRepository<Event, Long> {

    @Query("select ev " +
            "from Event as ev " +
            "where ev.initiator.id = ?1 " +
            "order by ev.id asc " +
            "limit ?2 offset ?3")
    List<Event> findByInitiatorId(Long userId, Integer size, Integer from);

    Event findByInitiatorIdAndId(Long userId, Long eventId);

    List<Event> findAllByIdIn(List<Long> Ids);

    boolean existsByCategory(Category category);
}
