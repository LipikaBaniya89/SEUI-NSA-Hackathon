package com.example.disasterapi.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DisasterService {

    // ---------------- Utility Functions ----------------
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private Map.Entry<String, Map<String, Object>> classifyEvent(Map<String, Object> event, String category, Map<String, Object> extra) {
        boolean emergency = false;
        Map<String, Object> extraInfo = new HashMap<>();

        if ("Earthquake".equals(category)) {
            double mag = extra != null && extra.get("magnitude") != null ? ((Number) extra.get("magnitude")).doubleValue() : 0;
            Double depth = extra != null ? ((Number) extra.get("depth")).doubleValue() : null;
            if (mag >= 5.5) emergency = true;
            String severity = mag >= 5.5 ? "Damaging (Emergency)" : "Minor (Non-Emergency)";
            extraInfo.put("magnitude", mag);
            extraInfo.put("depth_km", depth);
            extraInfo.put("severity", severity);
        } else if ("Wildfires".equals(category)) {
            String title = ((String) event.get("title")).toLowerCase();
            if (title.contains("controlled") || title.contains("prescribed")) extraInfo.put("fire_status", "Controlled Fire");
            else if (title.contains("contained")) { extraInfo.put("fire_status", "Partially Contained Fire"); emergency = true; }
            else { extraInfo.put("fire_status", "Uncontrolled Wildfire"); emergency = true; }
        } else if ("Storms".equals(category) || "Severe Storm".equals(category)) {
            emergency = true;
            extraInfo.put("storm_status", "Severe Weather Warning");
        } else if ("Volcanoes".equals(category)) {
            if (((String) event.get("title")).toLowerCase().contains("eruption")) { emergency = true; extraInfo.put("volcano_status", "Eruption Ongoing"); }
            else extraInfo.put("volcano_status", "Monitoring Activity");
        } else {
            extraInfo.put("note", "No detailed classification available");
        }

        return Map.entry(emergency ? "Emergency" : "Non-Emergency", extraInfo);
    }

    // ---------------- Public API Method ----------------
    public List<Map<String, Object>> getDisasters(String location, int radius) {

        List<Map<String, Object>> geoData = API.geocodeLocation(location);
        if (geoData == null || geoData.isEmpty()) return List.of(Map.of("error", "Could not geocode location: " + location));

        double lat = Double.parseDouble((String) geoData.get(0).get("lat"));
        double lon = Double.parseDouble((String) geoData.get(0).get("lon"));

        List<Map<String, Object>> results = new ArrayList<>();

        // NASA Events
        Map<String, Object> nasaData = API.fetchNasaEvents();
        List<Map<String, Object>> events = (List<Map<String, Object>>) nasaData.get("events");
        if (events != null) {
            for (Map<String, Object> event : events) {
                List<Map<String, Object>> geometry = (List<Map<String, Object>>) event.get("geometry");
                if (geometry != null) {
                    for (Map<String, Object> geo : geometry) {
                        List<Object> coords = (List<Object>) geo.get("coordinates");
                        if (coords.size() >= 2) {
                            double eventLon = ((Number) coords.get(0)).doubleValue();
                            double eventLat = ((Number) coords.get(1)).doubleValue();
                            double distance = haversine(lat, lon, eventLat, eventLon);
                            if (distance <= radius) {
                                List<Map<String, Object>> categories = (List<Map<String, Object>>) event.get("categories");
                                String category = categories != null && !categories.isEmpty() ? (String) categories.get(0).get("title") : "Unknown";
                                Map.Entry<String, Map<String, Object>> classification = classifyEvent(event, category, null);
                                results.add(Map.of(
                                        "title", event.get("title"),
                                        "category", category,
                                        "distance_km", String.format("%.2f", distance),
                                        "classification", classification.getKey(),
                                        "extra_info", classification.getValue(),
                                        "link", event.get("link"),
                                        "coordinates", coords
                                ));
                            }
                        }
                    }
                }
            }
        }

        // USGS Earthquakes
        Map<String, Object> usgsData = API.fetchUsgsEarthquakes();
        List<Map<String, Object>> features = (List<Map<String, Object>>) usgsData.get("features");
        if (features != null) {
            for (Map<String, Object> quake : features) {
                Map<String, Object> geometry = (Map<String, Object>) quake.get("geometry");
                List<Object> coords = (List<Object>) geometry.get("coordinates");
                double quakeLon = ((Number) coords.get(0)).doubleValue();
                double quakeLat = ((Number) coords.get(1)).doubleValue();
                double depth = ((Number) coords.get(2)).doubleValue();

                Map<String, Object> properties = (Map<String, Object>) quake.get("properties");
                double mag = properties.get("mag") != null ? ((Number) properties.get("mag")).doubleValue() : 0.0;
                String place = (String) properties.get("place");

                double distance = haversine(lat, lon, quakeLat, quakeLon);
                if (distance <= radius) {
                    Map.Entry<String, Map<String, Object>> classification = classifyEvent(
                            Map.of("title", place), "Earthquake",
                            Map.of("magnitude", mag, "depth", depth)
                    );
                    results.add(Map.of(
                            "title", place,
                            "category", "Earthquake",
                            "distance_km", String.format("%.2f", distance),
                            "classification", classification.getKey(),
                            "extra_info", classification.getValue(),
                            "link", properties.get("url"),
                            "coordinates", coords
                    ));
                }
            }
        }

        if (results.isEmpty()) return List.of(Map.of("message", "No disasters found nearby."));
        return results;
    }
}
