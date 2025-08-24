package com.example.disasterapi.service;

import com.example.disasterapi.domain.AppUser;
import com.example.disasterapi.domain.Notification;
import com.example.disasterapi.domain.VolunteerRequest;
import com.example.disasterapi.repo.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final AppUserRepository users;
    private final NotificationService notifier;
    private final EmbeddingService embedding;

    @Value("${app.match.threshold:0.6}")
    private double threshold;

    public record MatchResult(Long userId, Long requestId, double score, List<String> matchedSkills, String message) {
    }

    public List<MatchResult> dispatchForRequest(VolunteerRequest req) {
        List<AppUser> all = users.findAll();
        float[] reqVec = embedding.embed(String.join(", ", req.getRequiredSkills()));
        List<MatchResult> out = new ArrayList<>();

        for (AppUser u : all) {
            if (!u.isEnabled()) continue;
            if (req.getCentroidLat() != null && req.getCentroidLon() != null) {
                if (distanceMeters(u.getLatitude(), u.getLongitude(), req.getCentroidLat(), req.getCentroidLon()) > req.getRadiusMeters())
                    continue;
            }
            float[] userVec = embedding.embed(String.join(", ", u.getSkills()));
            double score = cosine(userVec, reqVec);
            if (score >= threshold) {
                String msg = "Request "  + req.getHeadline() + " near you. Needed skills: " + String.join(", ", req.getRequiredSkills());
                notifier.saveAndDispatch(Notification.builder()
                        .userId(u.getId()).requestId(req.getId()).score(score).message(msg).build(), u);
                out.add(new MatchResult(u.getId(), req.getId(), score, new ArrayList<>(req.getRequiredSkills()), msg));
            }
        }
        out.sort((a, b) -> Double.compare(b.score, a.score));
        return out;
    }

    public static double distanceMeters(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371000.0;
        double dLat = Math.toRadians(lat2 - lat1), dLon = Math.toRadians(lon2 - lon1);
        double sLat = Math.sin(dLat / 2), sLon = Math.sin(dLon / 2);
        double h = sLat * sLat + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * sLon * sLon;
        return R * 2 * Math.atan2(Math.sqrt(h), Math.sqrt(1 - h));
    }

    private static double cosine(float[] a, float[] b) {
        int n = Math.min(a.length, b.length);
        double dot = 0, na = 0, nb = 0;
        for (int i = 0; i < n; i++) {
            dot += a[i] * b[i];
            na += a[i] * a[i];
            nb += b[i] * b[i];
        }
        if (na == 0 || nb == 0) return 0;
        return dot / (Math.sqrt(na) * Math.sqrt(nb));
    }

}
