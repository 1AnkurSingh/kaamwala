package com.kaamwala.repository;
import com.kaamwala.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    List<User> findByNameContaining(String keyword);

    //NEW METHODS FOR WORKER SEARCH:

    // Basic worker search with pagination
    List<User> findByRole(String role, Pageable pageable);

    // Find workers by category name through their skills
    @Query("SELECT DISTINCT u FROM User u " +
           "JOIN u.userSkills us " +
           "JOIN us.subCategory sc " +
           "JOIN sc.category c " +
           "WHERE u.role = 'worker' AND LOWER(c.categoryName) = LOWER(:categoryName)")
    List<User> findWorkersByCategory(@Param("categoryName") String categoryName, Pageable pageable);

    // Find workers by specific skill (subcategory name)
    @Query("SELECT DISTINCT u FROM User u " +
           "JOIN u.userSkills us " +
           "JOIN us.subCategory sc " +
           "WHERE u.role = 'worker' AND LOWER(sc.subCategoryName) = LOWER(:skillName)")
    List<User> findWorkersBySkill(@Param("skillName") String skillName, Pageable pageable);

    // Find workers by service area/location
    @Query("SELECT u FROM User u WHERE u.role = 'worker' AND " +
           "LOWER(u.serviceAreas) LIKE LOWER(CONCAT('%', :location, '%'))")
    List<User> findWorkersByServiceArea(@Param("location") String location, Pageable pageable);

    // Advanced search with multiple filters
    @Query("SELECT DISTINCT u FROM User u " +
           "LEFT JOIN u.userSkills us " +
           "LEFT JOIN us.subCategory sc " +
           "LEFT JOIN sc.category c " +
           "WHERE u.role = 'worker' " +
           "AND (:category IS NULL OR LOWER(c.categoryName) = LOWER(:category)) " +
           "AND (:skill IS NULL OR LOWER(sc.subCategoryName) = LOWER(:skill)) " +
           "AND (:location IS NULL OR LOWER(u.serviceAreas) LIKE LOWER(CONCAT('%', :location, '%'))) " +
           "AND (:minRate IS NULL OR u.hourlyRate >= :minRate) " +
           "AND (:maxRate IS NULL OR u.hourlyRate <= :maxRate) " +
           "AND (:minExperience IS NULL OR u.experience >= :minExperience)")
    List<User> findWorkersWithFilters(
        @Param("category") String category,
        @Param("skill") String skill,
        @Param("location") String location,
        @Param("minRate") Double minRate,
        @Param("maxRate") Double maxRate,
        @Param("minExperience") Integer minExperience,
        Pageable pageable
    );

    // Legacy methods (keeping for backward compatibility)
    List<User> findByRole(String role);
    
    @Query("SELECT u FROM User u WHERE u.role = 'worker' AND u.serviceAreas LIKE %:location%")
    List<User> findWorkersByLocation(@Param("location") String location);

    @Query("SELECT u FROM User u WHERE u.role = 'worker' AND u.hourlyRate <= :maxRate")
    List<User> findWorkersByMaxRate(@Param("maxRate") Double maxRate);
}