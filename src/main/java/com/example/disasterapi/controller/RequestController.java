package com.example.disasterapi.controller;


import com.example.disasterapi.domain.AppRole;
import com.example.disasterapi.domain.AppUser;
import com.example.disasterapi.domain.VolunteerRequest;
import com.example.disasterapi.domain.VolunteerRequestStatus;
import com.example.disasterapi.repo.AppUserRepository;
import com.example.disasterapi.repo.NotificationRepository;
import com.example.disasterapi.repo.VolunteerRequestRepository;
import com.example.disasterapi.service.MatchingService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestController {
    private final VolunteerRequestRepository requests;
    private final AppUserRepository users;
    private final NotificationRepository notifications;
    private final MatchingService matching;

    @PostMapping
    @PreAuthorize("hasRole('AUTHORITY')")
    public ResponseEntity<VolunteerRequest> create(Authentication auth, @Valid @RequestBody CreateRequest r) {
        AppUser me = users.findByEmail(auth.getName()).orElseThrow();
        if (me.getRole() != AppRole.AUTHORITY) return ResponseEntity.status(403).build();
        VolunteerRequest req = VolunteerRequest.builder()
                .authorityUserId(me.getId())
                .headline(r.getHeadline()).description(r.getDescription())
                .requiredSkills(Set.copyOf(r.getRequiredSkills() == null ? List.of() : r.getRequiredSkills()))
                .quantity(r.getQuantity() == null ? 1 : r.getQuantity())
                .centroidLat(r.getCentroidLat()).centroidLon(r.getCentroidLon())
                .radiusMeters(r.getRadiusMeters() == null ? 10000 : r.getRadiusMeters())
                .startsAt(r.getStartsAt() == null ? Instant.now() : r.getStartsAt())
                .expiresAt(r.getExpiresAt() == null ? Instant.now().plusSeconds(24 * 3600) : r.getExpiresAt())
                .status(VolunteerRequestStatus.OPEN)
                .build();
        return ResponseEntity.ok(requests.save(req));
    }

    @PostMapping("/{id}/dispatch")
    @PreAuthorize("hasRole('AUTHORITY')")
    public ResponseEntity<?> dispatch(Authentication auth, @PathVariable Long id) {
        AppUser me = users.findByEmail(auth.getName()).orElseThrow();
        VolunteerRequest req = requests.findById(id).orElseThrow();
        if (!req.getAuthorityUserId().equals(me.getId())) return ResponseEntity.status(403).body("Not your request");
        var results = matching.dispatchForRequest(req);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{id}/notifications")
    @PreAuthorize("hasRole('AUTHORITY')")
    public ResponseEntity<?> listNotifications(Authentication auth, @PathVariable Long id) {
        AppUser me = users.findByEmail(auth.getName()).orElseThrow();
        VolunteerRequest req = requests.findById(id).orElseThrow();
        if (!req.getAuthorityUserId().equals(me.getId())) return ResponseEntity.status(403).body("Not your request");
        return ResponseEntity.ok(notifications.findByRequestId(id));
    }

    @Data
    public static class CreateRequest {
        private String headline;
        private String description;
        private List<String> requiredSkills;
        private Integer quantity;
        private Double centroidLat;
        private Double centroidLon;
        private Integer radiusMeters;
        private Instant startsAt;
        private Instant expiresAt;
    }
}
