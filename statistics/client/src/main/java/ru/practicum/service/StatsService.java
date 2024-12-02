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
    public StatsService(@Value("http://localhost:9090") String serverUrl, RestTemplateBuilder builder) {
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

    public ResponseEntity<Object> getStats(List<String> uris, String start, String end, String unique) {
        Map<String, Object> parameters = new HashMap<>();
        String startCode = URLEncoder.encode(start, StandardCharsets.UTF_8);
        String endCode = URLEncoder.encode(end, StandardCharsets.UTF_8);
        parameters.put("startCode", startCode);
        parameters.put("endCode", endCode);
        parameters.put("uris", uris);
        parameters.put("unique", unique);
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }

    public ResponseEntity<Object> getExistByIpAndUri(String ip, String uri) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("requestIp", ip);
        parameters.put("uri", uri);
        return get("/stats/ip?requestIp={requestIp}&uri={uri}", parameters);
    }
}
