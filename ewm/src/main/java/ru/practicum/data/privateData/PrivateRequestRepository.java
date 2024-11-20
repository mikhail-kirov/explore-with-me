package ru.practicum.data.privateData;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Request;

import java.util.List;

public interface PrivateRequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByIdIn(List<Long> requestIds);

    List<Request> findAllByRequester(Long userId);

    List<Request> findByRequesterAndEvent(Long userId, Long eventId);

    List<Request> findByEvent(Long eventId);
}
