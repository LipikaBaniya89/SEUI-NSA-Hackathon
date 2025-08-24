package com.example.disasterapi.repo;

import com.example.disasterapi.domain.VolunteerRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolunteerRequestRepository extends JpaRepository<VolunteerRequest, Long> {
    List<VolunteerRequest> findByAuthorityUserId(Long authorityUserId);
}