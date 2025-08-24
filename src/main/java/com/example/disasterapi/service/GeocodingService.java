package com.example.disasterapi.service;

import java.util.Optional;



public interface GeocodingService {
    Optional<GeoResult> geocode(String address);
}
