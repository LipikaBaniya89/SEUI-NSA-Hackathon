package com.example.disasterapi.service;

import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

public class API { 

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String NASA_EONET_API = "https://eonet.gsfc.nasa.gov/api/v3/events";
    private static final String USGS_API = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson";
    private static final String GEOCODE_API = "https://nominatim.openstreetmap.org/search";

    public static Map<String, Object> fetchNasaEvents() {
        return restTemplate.getForObject(NASA_EONET_API, Map.class);
    }

    public static Map<String, Object> fetchUsgsEarthquakes() {
        return restTemplate.getForObject(USGS_API, Map.class);
    }

    public static List<Map<String, Object>> geocodeLocation(String location) {
        String url = GEOCODE_API + "?q=" + location + "&format=json&limit=1";
        return restTemplate.getForObject(url, List.class);
    }
}
