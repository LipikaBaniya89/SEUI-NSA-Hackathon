package com.example.disasterapi;

import com.example.disasterapi.service.DisasterService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("/api1")
public class DisasterApiController {

    private final DisasterService disasterService;

    public DisasterApiController(DisasterService disasterService) {
        this.disasterService = disasterService;
    }

    public static void main(String[] args) {
        SpringApplication.run(DisasterApiController.class, args);
    }

    // ---------------- JSON Endpoint ----------------
    @GetMapping(value = "/disasters", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Map<String, Object>> getDisasters(@RequestParam String location,
                                                  @RequestParam(defaultValue = "500") int radius) {
        return disasterService.getDisasters(location, radius);
    }
}
