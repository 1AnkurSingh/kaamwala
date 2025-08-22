package com.kaamwala.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_skills")
public class UserSkill {
    
    @Id
    @Column(name = "user_skill_id")
    private String userSkillId;
    
    // Many-to-One relationship with User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    // Many-to-One relationship with SubCategory (which represents the skill)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id", nullable = false)
    private SubCategory subCategory;
    
    // Worker's proficiency level in this skill
    @Enumerated(EnumType.STRING)
    @Column(name = "proficiency_level")
    private ProficiencyLevel proficiencyLevel;
    
    // Years of experience in this specific skill
    @Column(name = "experience_years")
    private Integer experienceYears;
    
    // Hourly rate for this specific skill (can be different from general hourly rate)
    @Column(name = "skill_hourly_rate")
    private Double skillHourlyRate;
    
    @Column(name = "is_primary_skill")
    @Builder.Default
    private Boolean isPrimarySkill = false; // Main skill of the worker
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    public enum ProficiencyLevel {
        BEGINNER,    // 0-2 years
        INTERMEDIATE, // 2-5 years
        ADVANCED,    // 5-10 years
        EXPERT       // 10+ years
    }
}
