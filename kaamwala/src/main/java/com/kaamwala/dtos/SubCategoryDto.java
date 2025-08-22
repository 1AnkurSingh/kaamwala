package com.kaamwala.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubCategoryDto {
    
    private String subCategoryId;
    
    @NotBlank(message = "Sub-category name is required")
    @Size(min = 2, max = 50, message = "Sub-category name must be between 2 and 50 characters")
    private String subCategoryName;
    
    @Size(max = 300, message = "Description cannot exceed 300 characters")
    private String description;
    
    private Boolean isActive = true;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Only include basic category info to avoid circular reference
    private String categoryId;
    private String categoryName;
}
