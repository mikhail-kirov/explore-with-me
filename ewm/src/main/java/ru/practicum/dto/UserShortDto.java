package ru.practicum.dto;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserShortDto {
    private Long id;
    private String name;
}
