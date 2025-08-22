package com.kaamwala.repository;

import com.kaamwala.model.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, String> {
    
    // Find all skills for a specific user
    List<UserSkill> findByUserUserId(String userId);
    
    // Find a specific user skill
    Optional<UserSkill> findByUserUserIdAndSubCategorySubCategoryId(String userId, String subCategoryId);
    
    // Find users who have a specific skill
    List<UserSkill> findBySubCategorySubCategoryId(String subCategoryId);
    
    // Find users by skill and proficiency level
    List<UserSkill> findBySubCategorySubCategoryIdAndProficiencyLevel(
        String subCategoryId, UserSkill.ProficiencyLevel proficiencyLevel);
    
    // Find primary skills for a user
    List<UserSkill> findByUserUserIdAndIsPrimarySkillTrue(String userId);
    
    // Check if user already has this skill
    boolean existsByUserUserIdAndSubCategorySubCategoryId(String userId, String subCategoryId);
    
    // Delete user skill
    void deleteByUserUserIdAndSubCategorySubCategoryId(String userId, String subCategoryId);
}
