package com.example.disasterapi.Model;


import jakarta.persistence.*;

@Entity
@Table(name = "user_skills")
public class UserSkill {

    @EmbeddedId
    private UserSkillId id;

    // Optional: map the fields from the composite key for convenience
    // This assumes a User entity exists
    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    public UserSkill() {}

    public UserSkill(Users user, String skill) {
        this.user = user;
        this.id = new UserSkillId(user.getId(), skill);
    }


}