# KaamWala Category System Guide

## Overview
The KaamWala platform now includes a comprehensive category system that organizes different skills and services into structured categories and subcategories. This system makes it easy for customers to find the right workers and for workers to showcase their specific skills.

## Category Structure

### Main Categories
The system includes 9 primary categories:

1. **Plumbing** - Water systems, pipes, fixtures installation and repair
2. **Electrical** - Wiring, installations, and electrical repairs  
3. **Carpentry** - Woodwork, furniture, and construction services
4. **Painting** - Interior, exterior, and decorative painting
5. **Cleaning** - Housekeeping and maintenance services
6. **Gardening** - Landscaping and plant care services
7. **Appliance Repair** - AC, fridge, washing machine repair services
8. **Masonry** - Brickwork, concrete, and tile services
9. **Welding** - Metal work and fabrication services

### Subcategories (Skills)
Each category contains 4 specific subcategories. For example:

**Plumbing Category:**
- Pipe Installation
- Tap & Faucet Repair
- Bathroom Fitting
- Water Tank Installation

**Electrical Category:**
- Home Wiring
- Appliance Installation
- Switch & Socket Repair
- Inverter & UPS Setup

*(And so on for all categories...)*

## API Endpoints

### Category Management

#### Get All Active Categories
```
GET /api/categories/active
```
Returns all active categories with their subcategories.

#### Get Category by ID
```
GET /api/categories/getById/{categoryId}
```

#### Search Categories
```
GET /api/categories/search/{keyword}
```

### Subcategory Management

#### Get Subcategories by Category
```
GET /api/categories/{categoryId}/subcategories
```

#### Search Subcategories
```
GET /api/categories/subcategories/search/{keyword}
```

### Worker Search APIs

#### Get All Workers
```
GET /api/workers/all
Parameters: page, size, sortBy
```

#### Search Workers by Category
```
GET /api/workers/search/category/{categoryName}
Example: /api/workers/search/category/Plumbing
```

#### Search Workers by Skill
```
GET /api/workers/search/skill/{skillName}
Example: /api/workers/search/skill/Pipe Installation
```

#### Search Workers by Location
```
GET /api/workers/search/location/{location}
Example: /api/workers/search/location/Delhi
```

#### Advanced Worker Search
```
GET /api/workers/search
Parameters:
- category: Category name (e.g., "Plumbing")
- skill: Specific skill name (e.g., "Pipe Installation")
- location: Service area (e.g., "Delhi")
- minRate: Minimum hourly rate
- maxRate: Maximum hourly rate
- minExperience: Minimum years of experience
- page, size, sortBy: Pagination parameters

Example: /api/workers/search?category=Plumbing&location=Delhi&minRate=100&maxRate=500
```

#### Get Top-Rated Workers
```
GET /api/workers/top-rated
```
Currently sorts by experience (will be enhanced with rating system)

#### Get Recently Joined Workers
```
GET /api/workers/recent
```

## Data Models

### Category
- categoryId (String, Primary Key)
- categoryName (String, Unique)
- description (String)
- iconName (String)
- isActive (Boolean)
- createdAt, updatedAt (LocalDateTime)
- subCategories (One-to-Many relationship)

### SubCategory  
- subCategoryId (String, Primary Key)
- subCategoryName (String)
- description (String)
- isActive (Boolean)
- createdAt, updatedAt (LocalDateTime)
- category (Many-to-One relationship)

### UserSkill (Junction Table)
- userSkillId (String, Primary Key)
- user (Many-to-One relationship)
- subCategory (Many-to-One relationship)
- proficiencyLevel (BEGINNER, INTERMEDIATE, ADVANCED, EXPERT)
- experienceYears (Integer)
- skillHourlyRate (Double)
- isPrimarySkill (Boolean)
- createdAt, updatedAt (LocalDateTime)

## Frontend Usage Examples

### Display Category List
```javascript
// Get all active categories
fetch('/api/categories/active')
  .then(response => response.json())
  .then(categories => {
    categories.forEach(category => {
      console.log(`${category.categoryName}: ${category.subCategories.length} skills`);
    });
  });
```

### Search for Plumbers in Delhi
```javascript
// Search for plumbers in Delhi
fetch('/api/workers/search?category=Plumbing&location=Delhi')
  .then(response => response.json())
  .then(workers => {
    console.log(`Found ${workers.length} plumbers in Delhi`);
    workers.forEach(worker => {
      console.log(`${worker.name} - ₹${worker.hourlyRate}/hour`);
    });
  });
```

### Get Specific Skill Workers
```javascript
// Find workers who can do pipe installation
fetch('/api/workers/search/skill/Pipe Installation')
  .then(response => response.json())
  .then(workers => {
    console.log(`Found ${workers.length} pipe installation specialists`);
  });
```

## Database Seeding

The application automatically creates default categories and subcategories on startup. The data includes:

- 9 main categories
- 36 subcategories (4 per category)
- All marked as active by default
- Unique IDs for each category and subcategory

## Benefits

1. **Structured Organization**: Clear hierarchy makes it easy to browse services
2. **Precise Search**: Customers can find exactly the skill they need
3. **Worker Specialization**: Workers can showcase specific expertise
4. **Scalable**: Easy to add new categories and skills as the platform grows
5. **User-Friendly**: Intuitive navigation for both customers and workers

## Future Enhancements

1. **Rating System**: Integrate worker ratings and reviews
2. **Skill Verification**: Certification and skill verification system
3. **Dynamic Pricing**: Category-based pricing recommendations
4. **Regional Categories**: Location-specific service categories
5. **Skill Matching**: AI-powered worker-customer matching
6. **Performance Analytics**: Category-wise performance metrics

## Usage in Registration

When workers register, they can:
1. Select their primary category
2. Choose multiple subcategories (skills)
3. Set proficiency levels for each skill
4. Define skill-specific hourly rates
5. Specify years of experience per skill

This creates a rich profile that helps customers find the perfect match for their needs.
