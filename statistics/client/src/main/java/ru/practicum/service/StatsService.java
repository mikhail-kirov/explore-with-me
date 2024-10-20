package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.config.BaseClient;
import ru.practicum.model.EndpointHitDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class StatsService extends BaseClient {

    @Autowired
    public StatsService(@Value("${api.base.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createHit(EndpointHitDto endpointHitDto) {
        return post("/hit", endpointHitDto);
    }

    public ResponseEntity<Object> getStats(List<String> uris, String start, String end, boolean unique) {
        Map<String, Object> parameters = new HashMap<>();
        StringBuilder uriBuilder = new StringBuilder();
        if (uris.size() > 1) {
            for (int i = 0; i < uris.size() - 1; i++) {
                uriBuilder.append(uris.get(i) + "&uris=");
            }
            uriBuilder.append(uris.getLast());
        } else {
            uriBuilder.append(uris.getFirst());
        }
        String startCode = URLEncoder.encode(start, StandardCharsets.UTF_8);
        String endCode = URLEncoder.encode(end, StandardCharsets.UTF_8);
        parameters.put("startCode", startCode);
        parameters.put("endCode", endCode);
        parameters.put("uris", uriBuilder);
        parameters.put("unique", unique);
        return get("/stats", parameters);
    }
}
