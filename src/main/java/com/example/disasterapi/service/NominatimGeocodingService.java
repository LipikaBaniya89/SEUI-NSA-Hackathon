package com.example.disasterapi.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@ConditionalOnProperty(name="app.geo.provider", havingValue="nominatim", matchIfMissing = true)
public class NominatimGeocodingService implements GeocodingService {
    private final RestClient http = RestClient.builder()
            .baseUrl("https://nominatim.openstreetmap.org").build();

    @Override
    public Optional<GeoResult> geocode(String address) {
        if (address == null || address.isBlank()) return Optional.empty();
        List<Map<String,Object>> res = http.get()
                .uri(uri -> uri.path("/search")
                        .queryParam("q", address)
                        .queryParam("format", "json")
                        .queryParam("limit", "1")
                        .build())
                .header("User-Agent", "volunteer-disaster-app/1.0 (+contact@example.com)")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        if (res == null || res.isEmpty()) return Optional.empty();
        Map<String,Object> r = res.get(0);
        Double lat = Double.valueOf(String.valueOf(r.get("lat")));
        Double lon = Double.valueOf(String.valueOf(r.get("lon")));
        String display = String.valueOf(r.get("display_name"));
        String classType = String.valueOf(r.get("class")) + ":" + String.valueOf(r.get("type"));
        return Optional.of(new GeoResult(lat, lon, display, classType));
    }
}
