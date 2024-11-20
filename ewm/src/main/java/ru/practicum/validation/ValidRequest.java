package ru.practicum.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.data.privateData.PrivateRequestRepository;
import ru.practicum.exeption.NotFoundException;
import ru.practicum.model.Request;

@Component
@Slf4j
@RequiredArgsConstructor
public class ValidRequest {

    private final PrivateRequestRepository privateRequestRepository;

    public Request validateRequestById(Long requestId) {
        return privateRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("The required object was not found.", "Request with id=" + requestId + " was not found"));
    }
}
