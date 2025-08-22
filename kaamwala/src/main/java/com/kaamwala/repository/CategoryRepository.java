package com.kaamwala.repository;

import com.kaamwala.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    
    // Find category by name (case insensitive)
    Optional<Category> findByCategoryNameIgnoreCase(String categoryName);
    
    // Find all active categories
    List<Category> findByIsActiveTrue();
    
    // Find categories containing keyword in name or description
    @Query("SELECT c FROM Category c WHERE " +
           "(LOWER(c.categoryName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "c.isActive = true")
    List<Category> findByKeywordAndActive(@Param("keyword") String keyword);
    
    // Check if category exists by name
    boolean existsByCategoryNameIgnoreCase(String categoryName);
}
