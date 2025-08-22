package com.kaamwala.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_email", unique = true)
    private String email;

    @Column(name = "user_Password", length = 15)
    private String password;

    private String gender;

    @Column(name = "about", length = 1000)
    private String about;

    @Column(name = "user_image_name")
    private String imageName;


    @Column(name = "phone_number", length = 15)
    private String phone;

    @Column(name = "user_role")
    private String role; // "worker" or "customer"

    @Column(name = "experience_years")
    private Integer experience;

    @Column(name = "hourly_rate")
    private Double hourlyRate;

    // One-to-Many relationship with UserSkill
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserSkill> userSkills = new HashSet<>();

    @Column(name = "service_areas", length = 500)
    private String serviceAreas;

    // Customer-specific fields
    @Column(name = "preferred_location", length = 200)
    private String preferredLocation;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Role> roles = new HashSet<>();
}