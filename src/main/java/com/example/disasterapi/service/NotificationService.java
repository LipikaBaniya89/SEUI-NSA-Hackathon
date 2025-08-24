package com.example.disasterapi.service;


import com.example.disasterapi.domain.AppUser;
import com.example.disasterapi.domain.Notification;
import com.example.disasterapi.notify.EmailSender;
import com.example.disasterapi.notify.SmsSender;
import com.example.disasterapi.repo.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationRepository repo;
    private final EmailSender emailSender; // conditional
    private final SmsSender smsSender;     // conditional

    @Value("${app.notify.email.enabled:true}")
    private boolean emailEnabled;
    @Value("${app.notify.sms.enabled:false}")
    private boolean smsEnabled;

    public Notification saveAndDispatch(Notification n, AppUser user) {
        Notification saved = repo.save(n);
        String body = n.getMessage() == null ? "You have a new volunteer request." : n.getMessage();
        try {
            if (emailEnabled && user.isWantsEmail()) {
                emailSender.send(user.getEmail(), "Volunteer Request", body);
            }
        } catch (Exception e) {
            log.warn("Email send failed for {}: {}", user.getEmail(), e.toString());
        }
        try {
            if (smsEnabled && user.isWantsSms() && user.getPhone() != null) {
                smsSender.send(user.getPhone(), body);
            }
        } catch (Exception e) {
            log.warn("SMS send failed for {}: {}", user.getPhone(), e.toString());
        }
        return saved;
    }
}
