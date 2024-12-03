package ru.practicum.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class LocationDto {

    Long id;

    @Length(min = 1, max = 300)
    private String name;

    @Length(min = 1)
    private String description;

    @NotNull
    private Double lat;

    @NotNull
    private Double lon;

    @Min(value = 0)
    @Max(value = 3000)
    private Float radius;

    private Long owner;
}
