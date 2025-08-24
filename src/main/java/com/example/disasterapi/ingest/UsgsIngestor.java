package com.example.disasterapi.ingest;


import com.example.disasterapi.domain.DisasterEvent;
import com.example.disasterapi.repo.DisasterEventRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class UsgsIngestor {
    private static final Logger log = LoggerFactory.getLogger(UsgsIngestor.class);

    @Value("${app.ingestors.usgs.enabled:true}")
    private boolean enabled;
    private final DisasterEventRepository repo;
    private final WebClient web = WebClient.create("https://earthquake.usgs.gov");
    private final ObjectMapper mapper = new ObjectMapper();

    @Scheduled(fixedDelayString = "${app.ingestors.usgs.pollMs:300000}")
    public void poll() {
        if (!enabled) return;
        try {
            String json = web.get().uri("/earthquakes/feed/v1.0/summary/all_day.geojson")
                    .retrieve().bodyToMono(String.class).block();
            JsonNode root = mapper.readTree(json);
            for (JsonNode f : root.path("features")) {
                String id = "USGS-" + f.path("id").asText();
                JsonNode props = f.path("properties");
                JsonNode geom = f.path("geometry");
                double lon = geom.path("coordinates").get(0).asDouble();
                double lat = geom.path("coordinates").get(1).asDouble();
                double mag = props.path("mag").asDouble(0.0);
                long t = props.path("time").asLong(System.currentTimeMillis());
                String title = props.path("title").asText("");
                log.info("Path is {}",props.path("place"));
                DisasterEvent ev = DisasterEvent.builder()
                        .id(id).source("USGS").type("earthquake").headline(title)
                        .description("M" + mag + " " + props.path("place").toString())
                        .centroidLat(lat).centroidLon(lon)
                        .severity(mag >= 6 ? "severe" : mag >= 4.5 ? "moderate" : "minor")
                        .urgency("immediate").certainty("observed")
                        .startsAt(Instant.ofEpochMilli(t))
                        .endsAt(Instant.ofEpochMilli(t).plusSeconds(6 * 3600)).build();
                repo.save(ev);
            }
            log.info("USGS ingested OK");
        } catch (Exception e) {
            log.warn("USGS ingest failed: {}", e.toString());
        }
    }
}
