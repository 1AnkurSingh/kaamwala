package com.kaamwala;

import com.kaamwala.model.Role;
import com.kaamwala.model.User;
import com.kaamwala.model.Category;
import com.kaamwala.model.SubCategory;
import com.kaamwala.repository.RoleRepository;
import com.kaamwala.repository.UserRepository;
import com.kaamwala.repository.CategoryRepository;
import com.kaamwala.repository.SubCategoryRepository;
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
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	SubCategoryRepository subCategoryRepository;

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
			
			// --- Create Default Categories and SubCategories ---
			createDefaultCategories();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error occurred while creating default users/roles.");
		}
	}
	
	private void createDefaultCategories() {
		try {
			// 1. Plumbing Category
			Category plumbingCategory = createCategoryIfNotExists("PLUMBING_CAT_001", "Plumbing", 
				"Water systems, pipes, fixtures installation and repair", "plumbing_icon.png");
			if (plumbingCategory != null) {
				createSubCategoryIfNotExists("PLUMB_SUB_001", "Pipe Installation", 
					"Installation of water and drainage pipes", plumbingCategory);
				createSubCategoryIfNotExists("PLUMB_SUB_002", "Tap & Faucet Repair", 
					"Fixing and installing taps, faucets, and valves", plumbingCategory);
				createSubCategoryIfNotExists("PLUMB_SUB_003", "Bathroom Fitting", 
					"Complete bathroom installation and repair", plumbingCategory);
				createSubCategoryIfNotExists("PLUMB_SUB_004", "Water Tank Installation", 
					"Water tank setup and maintenance", plumbingCategory);
			}

			// 2. Electrical Category
			Category electricalCategory = createCategoryIfNotExists("ELECTRICAL_CAT_002", "Electrical", 
				"Wiring, installations, and electrical repairs", "electrical_icon.png");
			if (electricalCategory != null) {
				createSubCategoryIfNotExists("ELEC_SUB_001", "Home Wiring", 
					"House electrical wiring installation and repair", electricalCategory);
				createSubCategoryIfNotExists("ELEC_SUB_002", "Appliance Installation", 
					"Installing fans, lights, and electrical appliances", electricalCategory);
				createSubCategoryIfNotExists("ELEC_SUB_003", "Switch & Socket Repair", 
					"Fixing electrical switches and sockets", electricalCategory);
				createSubCategoryIfNotExists("ELEC_SUB_004", "Inverter & UPS Setup", 
					"Backup power solutions installation", electricalCategory);
			}

			// 3. Carpentry Category
			Category carpentryCategory = createCategoryIfNotExists("CARPENTRY_CAT_003", "Carpentry", 
				"Woodwork, furniture, and construction services", "carpentry_icon.png");
			if (carpentryCategory != null) {
				createSubCategoryIfNotExists("CARP_SUB_001", "Furniture Making", 
					"Custom furniture design and construction", carpentryCategory);
				createSubCategoryIfNotExists("CARP_SUB_002", "Door & Window Installation", 
					"Installing and repairing doors and windows", carpentryCategory);
				createSubCategoryIfNotExists("CARP_SUB_003", "Cabinet & Shelving", 
					"Kitchen cabinets and storage solutions", carpentryCategory);
				createSubCategoryIfNotExists("CARP_SUB_004", "Wood Flooring", 
					"Wooden floor installation and polishing", carpentryCategory);
			}

			// 4. Painting Category
			Category paintingCategory = createCategoryIfNotExists("PAINTING_CAT_004", "Painting", 
				"Interior, exterior, and decorative painting", "painting_icon.png");
			if (paintingCategory != null) {
				createSubCategoryIfNotExists("PAINT_SUB_001", "Interior Painting", 
					"Indoor wall and ceiling painting", paintingCategory);
				createSubCategoryIfNotExists("PAINT_SUB_002", "Exterior Painting", 
					"Outdoor building painting and weather protection", paintingCategory);
				createSubCategoryIfNotExists("PAINT_SUB_003", "Decorative Painting", 
					"Artistic and textured wall designs", paintingCategory);
				createSubCategoryIfNotExists("PAINT_SUB_004", "Furniture Painting", 
					"Painting and finishing wooden furniture", paintingCategory);
			}

			// 5. Cleaning Category
			Category cleaningCategory = createCategoryIfNotExists("CLEANING_CAT_005", "Cleaning", 
				"Housekeeping and maintenance services", "cleaning_icon.png");
			if (cleaningCategory != null) {
				createSubCategoryIfNotExists("CLEAN_SUB_001", "House Cleaning", 
					"Regular household cleaning services", cleaningCategory);
				createSubCategoryIfNotExists("CLEAN_SUB_002", "Deep Cleaning", 
					"Comprehensive cleaning and sanitization", cleaningCategory);
				createSubCategoryIfNotExists("CLEAN_SUB_003", "Office Cleaning", 
					"Commercial space cleaning services", cleaningCategory);
				createSubCategoryIfNotExists("CLEAN_SUB_004", "Post-Construction Cleaning", 
					"Cleanup after construction or renovation", cleaningCategory);
			}

			// 6. Gardening Category
			Category gardeningCategory = createCategoryIfNotExists("GARDENING_CAT_006", "Gardening", 
				"Landscaping and plant care services", "gardening_icon.png");
			if (gardeningCategory != null) {
				createSubCategoryIfNotExists("GARDEN_SUB_001", "Lawn Maintenance", 
					"Grass cutting and lawn care", gardeningCategory);
				createSubCategoryIfNotExists("GARDEN_SUB_002", "Plant Care", 
					"Plant maintenance and disease treatment", gardeningCategory);
				createSubCategoryIfNotExists("GARDEN_SUB_003", "Landscaping", 
					"Garden design and landscape installation", gardeningCategory);
				createSubCategoryIfNotExists("GARDEN_SUB_004", "Tree Trimming", 
					"Tree cutting and pruning services", gardeningCategory);
			}

			// 7. Appliance Repair Category
			Category applianceCategory = createCategoryIfNotExists("APPLIANCE_CAT_007", "Appliance Repair", 
				"AC, fridge, washing machine repair services", "appliance_icon.png");
			if (applianceCategory != null) {
				createSubCategoryIfNotExists("APPL_SUB_001", "AC Repair", 
					"Air conditioner installation and repair", applianceCategory);
				createSubCategoryIfNotExists("APPL_SUB_002", "Refrigerator Repair", 
					"Fridge and freezer repair services", applianceCategory);
				createSubCategoryIfNotExists("APPL_SUB_003", "Washing Machine Repair", 
					"Laundry appliance repair and maintenance", applianceCategory);
				createSubCategoryIfNotExists("APPL_SUB_004", "TV & Electronics Repair", 
					"Television and electronic device repair", applianceCategory);
			}

			// 8. Masonry Category
			Category masonryCategory = createCategoryIfNotExists("MASONRY_CAT_008", "Masonry", 
				"Brickwork, concrete, and tile services", "masonry_icon.png");
			if (masonryCategory != null) {
				createSubCategoryIfNotExists("MASON_SUB_001", "Brickwork", 
					"Wall construction and brick laying", masonryCategory);
				createSubCategoryIfNotExists("MASON_SUB_002", "Concrete Work", 
					"Concrete mixing and foundation work", masonryCategory);
				createSubCategoryIfNotExists("MASON_SUB_003", "Tile Installation", 
					"Floor and wall tile fitting", masonryCategory);
				createSubCategoryIfNotExists("MASON_SUB_004", "Plastering", 
					"Wall plastering and finishing", masonryCategory);
			}

			// 9. Welding Category
			Category weldingCategory = createCategoryIfNotExists("WELDING_CAT_009", "Welding", 
				"Metal work and fabrication services", "welding_icon.png");
			if (weldingCategory != null) {
				createSubCategoryIfNotExists("WELD_SUB_001", "Metal Fabrication", 
					"Custom metal structure creation", weldingCategory);
				createSubCategoryIfNotExists("WELD_SUB_002", "Gate & Railing", 
					"Security gates and stair railings", weldingCategory);
				createSubCategoryIfNotExists("WELD_SUB_003", "Repair Work", 
					"Metal repair and maintenance", weldingCategory);
				createSubCategoryIfNotExists("WELD_SUB_004", "Sheet Metal Work", 
					"Roofing and sheet metal fabrication", weldingCategory);
			}

			System.out.println("Default categories and subcategories created successfully!");
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error occurred while creating default categories.");
		}
	}
	
	private Category createCategoryIfNotExists(String categoryId, String categoryName, String description, String iconName) {
		if (!categoryRepository.existsById(categoryId)) {
			Category category = Category.builder()
					.categoryId(categoryId)
					.categoryName(categoryName)
					.description(description)
					.iconName(iconName)
					.isActive(true)
					.build();
			return categoryRepository.save(category);
		}
		return categoryRepository.findById(categoryId).orElse(null);
	}
	
	private void createSubCategoryIfNotExists(String subCategoryId, String subCategoryName, String description, Category category) {
		if (!subCategoryRepository.existsById(subCategoryId)) {
			SubCategory subCategory = SubCategory.builder()
					.subCategoryId(subCategoryId)
					.subCategoryName(subCategoryName)
					.description(description)
					.category(category)
					.isActive(true)
					.build();
			subCategoryRepository.save(subCategory);
		}
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
