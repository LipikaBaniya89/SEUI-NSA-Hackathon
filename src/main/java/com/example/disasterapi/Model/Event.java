package com.example.disasterapi.Model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @Column(length = 191)
    private String id;

    @Column(nullable = false, length = 50)
    private String source;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(length = 255)
    private String headline;

    @Lob
    private String description;

    private Double centroidLat;
    private Double centroidLon;

    @Column(length = 32)
    private String severity;

    @Column(length = 32)
    private String urgency;

    @Column(length = 32)
    private String certainty;

    private Instant startsAt;
    private Instant endsAt;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        // Set creation timestamp before persisting
        this.createdAt = Instant.now();
    }

    public Event(){

    }
    public Event(String id, String source, String type, String headline, String description, Double centroidLat, Double centroidLon, String severity, String urgency, String certainty, Instant startsAt, Instant endsAt, Instant createdAt) {
        this.id = id;
        this.source = source;
        this.type = type;
        this.headline = headline;
        this.description = description;
        this.centroidLat = centroidLat;
        this.centroidLon = centroidLon;
        this.severity = severity;
        this.urgency = urgency;
        this.certainty = certainty;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getCentroidLat() {
        return centroidLat;
    }

    public void setCentroidLat(Double centroidLat) {
        this.centroidLat = centroidLat;
    }

    public Double getCentroidLon() {
        return centroidLon;
    }

    public void setCentroidLon(Double centroidLon) {
        this.centroidLon = centroidLon;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getCertainty() {
        return certainty;
    }

    public void setCertainty(String certainty) {
        this.certainty = certainty;
    }

    public Instant getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(Instant startsAt) {
        this.startsAt = startsAt;
    }

    public Instant getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(Instant endsAt) {
        this.endsAt = endsAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    // Getters and setters
}
