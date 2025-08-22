package com.kaamwala;

import com.kaamwala.model.Role;
import com.kaamwala.model.User;
import com.kaamwala.repository.RoleRepository;
import com.kaamwala.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@SpringBootApplication
@EnableWebMvc
public class KaamwalaApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(KaamwalaApplication.class, args);
	}
	@Autowired
	private RoleRepository roleRepository;
	@Value("${normal.role.id}")
	private String role_normal_id;

	@Value("${admin.role.id}")
	private String role_admin_id;

	@Autowired
	UserRepository userRepository;

	@Override
	public void run(String... args) throws Exception {
		try {
			// --- Create Roles ---
			Role roleAdmin = Role.builder()
					.roleId(role_admin_id)
					.roleName("ROLE_ADMIN")
					.build();

			Role roleNormal = Role.builder()
					.roleId(role_normal_id)
					.roleName("ROLE_NORMAL")
					.build();

			// Save roles to DB only if not already present
			if (!roleRepository.existsById(role_admin_id)) {
				roleRepository.save(roleAdmin);
			}

			if (!roleRepository.existsById(role_normal_id)) {
				roleRepository.save(roleNormal);
			}

			// --- Create Admin User ---
			User adminUser = User.builder()
					.userId(UUID.randomUUID().toString())
					.name("Admin")
					.email("admin@gmail.com")
					.password("anku123") // Consider encoding in future
					.gender("Male")
					.about("I am Admin User")
					.imageName("default.png")
					.createdAt(LocalDateTime.now())
					.roles(Set.of(roleAdmin, roleNormal))  // Admin has both roles
					.build();

			// --- Create Normal User ---
			User normalUser = User.builder()
					.userId(UUID.randomUUID().toString())
					.name("Ankur Singh")
					.email("ankur@gmail.com")
					.password("ankur123")
					.gender("Male")
					.about("I am Normal User")
					.imageName("default.png")
					.createdAt(LocalDateTime.now())
					.roles(Set.of(roleNormal))
					.build();

			// Save users to DB only if email not already present
			if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
				userRepository.save(adminUser);
			}

			if (userRepository.findByEmail("ankur@gmail.com").isEmpty()) {
				userRepository.save(normalUser);
			}

			System.out.println("Default roles and users created successfully.");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error occurred while creating default users/roles.");
		}
	}




	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
