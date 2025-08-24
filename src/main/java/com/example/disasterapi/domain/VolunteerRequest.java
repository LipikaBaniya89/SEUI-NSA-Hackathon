package com.example.disasterapi.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "volunteer_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VolunteerRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "authority_user_id", nullable = false)
    private Long authorityUserId;

    private String headline;
    @Column(length = 1000)
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "volunteer_request_skills", joinColumns = @JoinColumn(name = "request_id"))
    @Column(name = "skill", nullable = false)
    private Set<String> requiredSkills = new HashSet<>();

    @Column(nullable = false)
    private Integer quantity = 1;
    @Column(name = "centroid_lat")
    private Double centroidLat;
    @Column(name = "centroid_lon")
    private Double centroidLon;
    @Column(name = "radius_meters", nullable = false)
    private Integer radiusMeters = 10000;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VolunteerRequestStatus status = VolunteerRequestStatus.OPEN;

    @Column(name = "starts_at", columnDefinition = "DATETIME")
    private Instant startsAt;
    @Column(name = "expires_at", columnDefinition = "DATETIME")
    private Instant expiresAt;

    @Column(name = "created_at", updatable = false, insertable = false, columnDefinition = "DATETIME")
    private Instant createdAt;
}
