package com.example.disasterapi.Model;


import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "notifications")
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    private Double score;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(length = 1000)
    private String message;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
    }
public Notifications(){

}
    public Notifications(Long id, Users user, Event event, Double score, Instant createdAt, String message) {
        this.id = id;
        this.user = user;
        this.event = event;
        this.score = score;
        this.createdAt = createdAt;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Getters and setters
}