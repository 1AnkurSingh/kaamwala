package com.kaamwala.repository;
import com.kaamwala.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    List<User> findByNameContaining(String keyword);

    //NEW METHODS:

    List<User> findByRole(String role);

    List<User> findByRoleAndSkillsIn(String role, List<String> skills);

    @Query("SELECT u FROM User u WHERE u.role = 'worker' AND u.serviceAreas LIKE %:location%")
    List<User> findWorkersByLocation(@Param("location") String location);

    @Query("SELECT u FROM User u WHERE u.role = 'worker' AND u.hourlyRate <= :maxRate")
    List<User> findWorkersByMaxRate(@Param("maxRate") Double maxRate);
}