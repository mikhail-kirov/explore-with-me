package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.config.BaseClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class ExternalService extends BaseClient {

    @Autowired
    public ExternalService(@Value("https://api.opencagedata.com") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public Boolean validateCoordinatesByCountry(Double lat, Double lon, String country) {

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("q", lat.toString() + "%2C" + lon.toString());
        parameters.put("key", "65093105699244d99f7051092088777e");
        parameters.put("language", "ru");
        parameters.put("pretty", 1);
        parameters.put("address_only", 1);

        ResponseEntity<Object> result = get("/geocode/v1/json?q={q}&key={key}&language={language}&pretty={pretty}&address_only={address_only}", parameters);

        String address = result.getBody().toString();

        return address.contains(country);
    }
}
