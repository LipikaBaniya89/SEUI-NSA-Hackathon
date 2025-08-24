package com.example.disasterapi.repo;



import com.example.disasterapi.domain.DisasterEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;


public interface DisasterEventRepository extends JpaRepository<DisasterEvent, String> {
    List<DisasterEvent> findByEndsAtAfter(Instant after);
}