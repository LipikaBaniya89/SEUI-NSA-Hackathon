package com.example.disasterapi.Model;


import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(schema = "sql5795879",name="users")
public class Users {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true)
        private String email;

        @Column(name = "password_hash", nullable = false)
        private String passwordHash;

        @Column(name = "first_name", nullable = false)
        private String firstName;

        @Column(name = "last_name", nullable = false)
        private String lastName;

        @Column(name ="enabled", nullable = false)
        private boolean enabled;

        @Column(name = "role", nullable = false)
        private String role;

        @Column(name = "latitude", nullable = false)
        private Double latitude;
        @Column(name = "longitude", nullable = false)
        private Double longitude;

        @Column(name = "radius", nullable = false)
        private Integer radius;

public Users(){

}
    public Users(Long id, String email, String passwordHash, String firstName, String lastName, boolean enabled, String role, Double latitude, Double longitude, Integer radius) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = enabled;
        this.role = role;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }
}
