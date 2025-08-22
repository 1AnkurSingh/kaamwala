package com.kaamwala.repository;

import com.kaamwala.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, String> {
    
    // Find subcategories by category ID
    List<SubCategory> findByCategoryCategoryIdAndIsActiveTrue(String categoryId);
    
    // Find subcategory by name and category
    Optional<SubCategory> findBySubCategoryNameIgnoreCaseAndCategoryCategoryId(
        String subCategoryName, String categoryId);
    
    // Find all active subcategories
    List<SubCategory> findByIsActiveTrue();
    
    // Find subcategories by keyword
    @Query("SELECT sc FROM SubCategory sc WHERE " +
           "(LOWER(sc.subCategoryName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(sc.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "sc.isActive = true")
    List<SubCategory> findByKeywordAndActive(@Param("keyword") String keyword);
    
    // Check if subcategory exists by name and category
    boolean existsBySubCategoryNameIgnoreCaseAndCategoryCategoryId(
        String subCategoryName, String categoryId);
}
