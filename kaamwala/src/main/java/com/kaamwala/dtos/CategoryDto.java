package com.kaamwala.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
    
    private String categoryId;
    
    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters")
    private String categoryName;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    private String iconName;
    
    private Boolean isActive = true;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // List of subcategories under this category
    private Set<SubCategoryDto> subCategories;
}
