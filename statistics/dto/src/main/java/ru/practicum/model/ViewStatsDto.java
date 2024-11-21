package ru.practicum.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewStatsDto implements Serializable {
    private String app;
    private String uri;
    private Long hits;
}
