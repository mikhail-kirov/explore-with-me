package ru.practicum.dto;

import lombok.*;
import ru.practicum.model.EventRequestStatus;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private EventRequestStatus status;
}
