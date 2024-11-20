package ru.practicum.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ViewStatsDto implements Serializable {
    private String app;
    private String uri;
    private Long hits;
    private String ip;

    public ViewStatsDto(String app, String uri, Long hits, String ip) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
        this.ip = ip;
    }
}
