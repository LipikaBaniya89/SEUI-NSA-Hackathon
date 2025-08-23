package com.example.disasterapi.Repository;


import com.example.disasterapi.Model.UserSkill;
import com.example.disasterapi.Model.UserSkillId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, UserSkillId> {
    // Custom method to find all skills for a given user ID
    List<UserSkill> findByIdUserId(Long userId);
}