package com.example.disasterapi.Model;


import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserSkillId implements Serializable {

    private Long userId;
    private String skill;

    public UserSkillId() {
    }

    public UserSkillId(Long userId, String skill) {
        this.userId = userId;
        this.skill = skill;
    }

    // Getters
    public Long getUserId() {
        return userId;
    }

    public String getSkill() {
        return skill;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSkillId that = (UserSkillId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(skill, that.skill);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, skill);
    }
}
