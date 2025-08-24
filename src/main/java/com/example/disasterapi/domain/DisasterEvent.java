package com.example.disasterapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisasterEvent {
    @Id
    @Column(length = 191)
    private String id;
    private String source;
    private String type;
    private String headline;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(name = "centroid_lat")
    private Double centroidLat;
    @Column(name = "centroid_lon")
    private Double centroidLon;
    private String severity;
    private String urgency;
    private String certainty;
    @Column(name = "starts_at", columnDefinition = "DATETIME")
    private Instant startsAt;
    @Column(name = "ends_at", columnDefinition = "DATETIME")
    private Instant endsAt;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false, insertable = false, columnDefinition = "DATETIME")
    private Instant createdAt;
}
