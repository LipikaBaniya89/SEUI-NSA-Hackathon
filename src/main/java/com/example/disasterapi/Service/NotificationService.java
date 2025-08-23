package com.example.disasterapi.Service;

import com.example.disasterapi.Model.Notifications;
import com.example.disasterapi.Repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notifications> getNotificationsForUser(Long userId) {
        return notificationRepository.findByUserId(userId);
    }
}
