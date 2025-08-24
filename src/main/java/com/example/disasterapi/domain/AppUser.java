package com.example.disasterapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppRole role;

    @Column(nullable = false)
    private boolean enabled = true;

    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;

    @Column(name = "radius_meters", nullable = false)
    private Integer radiusMeters = 10000;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="user_skills", joinColumns=@JoinColumn(name="user_id"))
    @Column(name="skill", nullable=false, length = 100)  // ensure 100, not 255
    private Set<String> skills = new HashSet<>();

    private String phone;
    @Column(nullable = false)
    private boolean wantsEmail = true;
    @Column(nullable = false)
    private boolean wantsSms = false;
}
