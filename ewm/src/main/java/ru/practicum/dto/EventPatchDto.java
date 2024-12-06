package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.model.Location;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class EventPatchDto {

    @Length(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @Length(min = 20, max = 7000)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;

    private String stateAction;

    @Length(min = 3, max = 120)
    private String title;
}
