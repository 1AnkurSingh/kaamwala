package com.kaamwala.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String userId;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be Male, Female, or Other")
    private String gender;

    @Size(max = 1000, message = "About must not exceed 1000 characters")
    private String about;

    private String imageName;

    // ✅ NEW FIELDS ADDED:

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(\\+91\\s?)?[789]\\d{9}$", message = "Please provide a valid Indian phone number")
    private String phone;

    @NotBlank(message = "User role is required")
    @Pattern(regexp = "^(worker|customer)$", message = "Role must be worker or customer")
    private String role;

    // Worker-specific fields (optional for customers)
    @Min(value = 0, message = "Experience cannot be negative")
    @Max(value = 50, message = "Experience cannot exceed 50 years")
    private Integer experience;

    @Min(value = 50, message = "Hourly rate should be at least ₹50")
    @Max(value = 5000, message = "Hourly rate cannot exceed ₹5000")
    private Double hourlyRate;

    private Set<UserSkillDto> userSkills;

    @Size(max = 500, message = "Service areas cannot exceed 500 characters")
    private String serviceAreas;

    // Customer-specific fields (optional for workers)
    @Size(max = 200, message = "Preferred location cannot exceed 200 characters")
    private String preferredLocation;

    // Backend will set this automatically
    private LocalDateTime createdAt;
}