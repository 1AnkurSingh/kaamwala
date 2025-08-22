package com.kaamwala.service.imp;

import com.kaamwala.dtos.PageableResponse;
import com.kaamwala.dtos.UserDto;
import com.kaamwala.exception.ResourceNotFoundException;
import com.kaamwala.helper.Helper;
import com.kaamwala.model.Role;
import com.kaamwala.model.User;
import com.kaamwala.repository.RoleRepository;
import com.kaamwala.repository.UserRepository;
import com.kaamwala.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ModelMapper mapper;
    @Autowired
    RoleRepository roleRepository;

    @Value("${user.profile.image.path}")
    private String imagePath;
    @Value("${normal.role.id}")
    private String normalRoleId;

    Logger logger = LoggerFactory.getLogger(UserServiceImp.class);

    @Override
    public UserDto createUser(UserDto userDto) {
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        userDto.setPassword(userDto.getPassword());

        // ✅ NEW: Set default values for worker fields
        if ("worker".equals(userDto.getRole())) {
            if (userDto.getExperience() == null) userDto.setExperience(0);
            if (userDto.getHourlyRate() == null) userDto.setHourlyRate(100.0);
            if (userDto.getSkills() == null) userDto.setSkills(new HashSet<>());
            if (userDto.getServiceAreas() == null) userDto.setServiceAreas("");
        }

        // Set default values for customer fields
        if ("customer".equals(userDto.getRole())) {
            if (userDto.getPreferredLocation() == null) userDto.setPreferredLocation("");
        }

        User user = mapper.map(userDto, User.class);

        //  ORIGINAL ROLE LOGIC - UNCHANGED
        Role role = roleRepository.findById(normalRoleId).get();
        user.getRoles().add(role);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        return mapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User id not found"));
        return mapper.map(user, UserDto.class);
    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ?
                (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        Page<User> page = userRepository.findAll(pageable);
        PageableResponse<UserDto> pageableResponse = Helper.getPageableResponse(page, UserDto.class);
        return pageableResponse;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User Email not found"));
        return mapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> getUSerByKeyword(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword);
        List<UserDto> collect = users.stream()
                .map(user -> mapper.map(user, UserDto.class))
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public Optional<User> findUserByEmailOptional(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Id Not Found!!"));

        // UPDATED: Handle all new fields
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setPassword(userDto.getPassword());
        user.setGender(userDto.getGender());
        user.setImageName(userDto.getImageName());

        //NEW: Update new fields
        user.setPhone(userDto.getPhone());
        user.setRole(userDto.getRole());
        user.setExperience(userDto.getExperience());
        user.setHourlyRate(userDto.getHourlyRate());
        user.setSkills(userDto.getSkills());
        user.setServiceAreas(userDto.getServiceAreas());
        user.setPreferredLocation(userDto.getPreferredLocation());

        User updatedUser = userRepository.save(user);
        return mapper.map(updatedUser, UserDto.class);
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User id not found!!"));

        // Delete image if exists
        if (user.getImageName() != null) {
            try {
                Path path = Paths.get(user.getImageName());
                Files.deleteIfExists(path);
            } catch (IOException ex) {
                logger.info("User image not found in folder!!");
                ex.printStackTrace();
            }
        }

        // Delete user
        userRepository.delete(user);
    }

    //  NEW: Add method to get users by role
    public List<UserDto> getUsersByRole(String role) {
        List<User> users = userRepository.findByRole(role);
        return users.stream()
                .map(user -> mapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    //  NEW: Add method to get workers by skills
    public List<UserDto> getWorkersBySkills(List<String> skills) {
        List<User> workers = userRepository.findByRoleAndSkillsIn("worker", skills);
        return workers.stream()
                .map(user -> mapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }
}