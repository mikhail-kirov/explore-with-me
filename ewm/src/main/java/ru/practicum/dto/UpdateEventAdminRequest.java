package ru.practicum.dto;

import jakarta.validation.constraints.Size;
import lombok.*;
import ru.practicum.model.StateAction;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UpdateEventAdminRequest {

    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000)
    private String description;

    private String eventDate;
    private LocationShortDto locationShortDto;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;

    @Size(min = 3, max = 120)
    private String title;
}
