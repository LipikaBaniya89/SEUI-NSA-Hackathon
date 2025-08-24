package com.example.disasterapi.repo;

import com.example.disasterapi.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRequestId(Long requestId);
}