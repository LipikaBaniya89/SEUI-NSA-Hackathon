package com.example.disasterapi.controller;


import com.example.disasterapi.repo.AppUserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final AppUserRepository users;

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {
        String email = auth.getName();
        return users.findByEmail(email).<ResponseEntity<?>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/me")
    public ResponseEntity<?> update(Authentication auth, @RequestBody UpdateProfile r) {
        var u = users.findByEmail(auth.getName()).orElseThrow();
        if (r.getFirstName() != null) u.setFirstName(r.getFirstName());
        if (r.getLastName() != null) u.setLastName(r.getLastName());
        if (r.getLatitude() != null) u.setLatitude(r.getLatitude());
        if (r.getLongitude() != null) u.setLongitude(r.getLongitude());
        if (r.getRadiusMeters() != null) u.setRadiusMeters(r.getRadiusMeters());
        if (r.getSkills() != null) u.setSkills(new HashSet<>(r.getSkills()));
        if (r.getPhone() != null) u.setPhone(r.getPhone());
        if (r.getWantsEmail() != null) u.setWantsEmail(r.getWantsEmail());
        if (r.getWantsSms() != null) u.setWantsSms(r.getWantsSms());
        users.save(u);
        return ResponseEntity.ok(u);
    }

    @Data
    public static class UpdateProfile {
        private String firstName;
        private String lastName;
        private Double latitude;
        private Double longitude;
        private Integer radiusMeters;
        private List<String> skills;
        private String phone;
        private Boolean wantsEmail;
        private Boolean wantsSms;
    }
}
