package com.kaamwala.dtos;

import com.kaamwala.model.UserSkill;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSkillDto {
    
    private String userSkillId;
    
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotBlank(message = "Subcategory ID is required")
    private String subCategoryId;
    
    // For display purposes
    private String subCategoryName;
    private String categoryName;
    
    @NotNull(message = "Proficiency level is required")
    private UserSkill.ProficiencyLevel proficiencyLevel;
    
    @Min(value = 0, message = "Experience years cannot be negative")
    @Max(value = 50, message = "Experience years cannot exceed 50")
    private Integer experienceYears;
    
    @Min(value = 50, message = "Skill hourly rate should be at least ₹50")
    @Max(value = 10000, message = "Skill hourly rate cannot exceed ₹10000")
    private Double skillHourlyRate;
    
    private Boolean isPrimarySkill = false;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
