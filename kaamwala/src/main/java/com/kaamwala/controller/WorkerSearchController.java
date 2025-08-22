package com.kaamwala.controller;

import com.kaamwala.dtos.UserDto;
import com.kaamwala.model.User;
import com.kaamwala.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/workers")
@CrossOrigin(origins = "http://localhost:3000")
public class WorkerSearchController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ModelMapper mapper;
    
    /**
     * Get all workers (users with role = 'worker')
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllWorkers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        List<User> workers = userRepository.findByRole("worker", pageable);
        
        List<UserDto> workerDtos = workers.stream()
                .map(worker -> mapper.map(worker, UserDto.class))
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(workerDtos, HttpStatus.OK);
    }
    
    /**
     * Search workers by category name
     * Example: /api/workers/search/category/Plumbing
     */
    @GetMapping("/search/category/{categoryName}")
    public ResponseEntity<List<UserDto>> getWorkersByCategory(
            @PathVariable("categoryName") String categoryName,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        List<User> workers = userRepository.findWorkersByCategory(categoryName, pageable);
        
        List<UserDto> workerDtos = workers.stream()
                .map(worker -> mapper.map(worker, UserDto.class))
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(workerDtos, HttpStatus.OK);
    }
    
    /**
     * Search workers by subcategory (specific skill)
     * Example: /api/workers/search/skill/Pipe Installation
     */
    @GetMapping("/search/skill/{skillName}")
    public ResponseEntity<List<UserDto>> getWorkersBySkill(
            @PathVariable("skillName") String skillName,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        List<User> workers = userRepository.findWorkersBySkill(skillName, pageable);
        
        List<UserDto> workerDtos = workers.stream()
                .map(worker -> mapper.map(worker, UserDto.class))
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(workerDtos, HttpStatus.OK);
    }
    
    /**
     * Search workers by location (service areas)
     * Example: /api/workers/search/location/Delhi
     */
    @GetMapping("/search/location/{location}")
    public ResponseEntity<List<UserDto>> getWorkersByLocation(
            @PathVariable("location") String location,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        List<User> workers = userRepository.findWorkersByServiceArea(location, pageable);
        
        List<UserDto> workerDtos = workers.stream()
                .map(worker -> mapper.map(worker, UserDto.class))
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(workerDtos, HttpStatus.OK);
    }
    
    /**
     * Advanced search: Workers by category and location
     * Example: /api/workers/search?category=Plumbing&location=Delhi
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchWorkers(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "skill", required = false) String skill,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "minRate", required = false) Double minRate,
            @RequestParam(value = "maxRate", required = false) Double maxRate,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        List<User> workers = userRepository.findWorkersWithFilters(
            category, skill, location, minRate, maxRate, minExperience, pageable);
        
        List<UserDto> workerDtos = workers.stream()
                .map(worker -> mapper.map(worker, UserDto.class))
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(workerDtos, HttpStatus.OK);
    }
    
    /**
     * Get top-rated workers (placeholder for future rating system)
     */
    @GetMapping("/top-rated")
    public ResponseEntity<List<UserDto>> getTopRatedWorkers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        
        // For now, sort by experience (highest first)
        Pageable pageable = PageRequest.of(page, size, Sort.by("experience").descending());
        List<User> workers = userRepository.findByRole("worker", pageable);
        
        List<UserDto> workerDtos = workers.stream()
                .map(worker -> mapper.map(worker, UserDto.class))
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(workerDtos, HttpStatus.OK);
    }
    
    /**
     * Get recently joined workers
     */
    @GetMapping("/recent")
    public ResponseEntity<List<UserDto>> getRecentWorkers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        List<User> workers = userRepository.findByRole("worker", pageable);
        
        List<UserDto> workerDtos = workers.stream()
                .map(worker -> mapper.map(worker, UserDto.class))
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(workerDtos, HttpStatus.OK);
    }
}
