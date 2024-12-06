package ru.practicum.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationPrivateDto {
    private Long id;
    private String name;
    private String description;
    private Double lat;
    private Double lon;
    private Float radius;
}
