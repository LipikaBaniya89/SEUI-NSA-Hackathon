package com.example.disasterapi.Repository;

import com.example.disasterapi.Model.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Long> {
    // Custom method to find notifications for a specific user ID
    List<Notifications> findByUserId(Long userId);
}
