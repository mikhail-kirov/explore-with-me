package ru.practicum.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ViewStatsDto {

    @NotNull
    private String app;

    @NotNull
    private String uri;

    private Integer hits;
}
