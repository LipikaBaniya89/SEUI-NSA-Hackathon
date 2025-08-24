package com.example.disasterapi.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "event_id", length = 191)
    private String eventId; // optional for event-based matches

    @Column(name = "request_id")
    private Long requestId; // for authority requests

    private Double score;

    @Column(name = "created_at", insertable = false, updatable = false, columnDefinition = "DATETIME")
    private Instant createdAt;

    @Column(length = 1000)
    private String message;
}
