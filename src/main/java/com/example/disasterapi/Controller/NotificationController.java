package com.example.disasterapi.Controller;

import com.example.disasterapi.Model.Notifications;
import com.example.disasterapi.Service.NotificationService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/user/{userId}")
    public List<Notifications> getNotificationsByUserId(@PathVariable Long userId) {
        return notificationService.getNotificationsForUser(userId);
    }
}