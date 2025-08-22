package com.kaamwala.service.imp;

import com.kaamwala.dtos.CategoryDto;
import com.kaamwala.dtos.PageableResponse;
import com.kaamwala.dtos.SubCategoryDto;
import com.kaamwala.exception.ResourceNotFoundException;
import com.kaamwala.helper.Helper;
import com.kaamwala.model.Category;
import com.kaamwala.model.SubCategory;
import com.kaamwala.repository.CategoryRepository;
import com.kaamwala.repository.SubCategoryRepository;
import com.kaamwala.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImp implements CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    
    @Autowired
    private ModelMapper mapper;
    
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        // Check if category already exists
        if (categoryRepository.existsByCategoryNameIgnoreCase(categoryDto.getCategoryName())) {
            throw new RuntimeException("Category with name '" + categoryDto.getCategoryName() + "' already exists");
        }
        
        // Generate unique ID
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
        
        // Convert DTO to Entity
        Category category = mapper.map(categoryDto, Category.class);
        
        // Save category
        Category savedCategory = categoryRepository.save(category);
        
        // Convert back to DTO
        return mapper.map(savedCategory, CategoryDto.class);
    }
    
    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {
        // Find existing category
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
        
        // Check if new name conflicts with existing category (excluding current one)
        if (!category.getCategoryName().equalsIgnoreCase(categoryDto.getCategoryName()) &&
            categoryRepository.existsByCategoryNameIgnoreCase(categoryDto.getCategoryName())) {
            throw new RuntimeException("Category with name '" + categoryDto.getCategoryName() + "' already exists");
        }
        
        // Update fields
        category.setCategoryName(categoryDto.getCategoryName());
        category.setDescription(categoryDto.getDescription());
        category.setIconName(categoryDto.getIconName());
        category.setIsActive(categoryDto.getIsActive());
        
        // Save updated category
        Category updatedCategory = categoryRepository.save(category);
        
        return mapper.map(updatedCategory, CategoryDto.class);
    }
    
    @Override
    public void deleteCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
        
        // Soft delete - mark as inactive instead of hard delete
        category.setIsActive(false);
        categoryRepository.save(category);
        
        // Also deactivate all subcategories under this category
        List<SubCategory> subCategories = subCategoryRepository.findByCategoryCategoryIdAndIsActiveTrue(categoryId);
        subCategories.forEach(subCategory -> subCategory.setIsActive(false));
        subCategoryRepository.saveAll(subCategories);
    }
    
    @Override
    public CategoryDto getCategoryById(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
        
        CategoryDto categoryDto = mapper.map(category, CategoryDto.class);
        
        // Load subcategories
        List<SubCategory> subCategories = subCategoryRepository.findByCategoryCategoryIdAndIsActiveTrue(categoryId);
        categoryDto.setSubCategories(subCategories.stream()
                .map(subCategory -> mapper.map(subCategory, SubCategoryDto.class))
                .collect(Collectors.toSet()));
        
        return categoryDto;
    }
    
    @Override
    public CategoryDto getCategoryByName(String categoryName) {
        Category category = categoryRepository.findByCategoryNameIgnoreCase(categoryName)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Name", categoryName));
        
        return mapper.map(category, CategoryDto.class);
    }
    
    @Override
    public PageableResponse<CategoryDto> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        
        Page<Category> page = categoryRepository.findAll(pageable);
        
        PageableResponse<CategoryDto> response = Helper.getPageableResponse(page, CategoryDto.class);
        return response;
    }
    
    @Override
    public List<CategoryDto> getAllActiveCategories() {
        List<Category> categories = categoryRepository.findByIsActiveTrue();
        return categories.stream()
                .map(category -> {
                    CategoryDto dto = mapper.map(category, CategoryDto.class);
                    // Load subcategories for each category
                    List<SubCategory> subCategories = subCategoryRepository.findByCategoryCategoryIdAndIsActiveTrue(category.getCategoryId());
                    dto.setSubCategories(subCategories.stream()
                            .map(subCategory -> mapper.map(subCategory, SubCategoryDto.class))
                            .collect(Collectors.toSet()));
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CategoryDto> searchCategories(String keyword) {
        List<Category> categories = categoryRepository.findByKeywordAndActive(keyword);
        return categories.stream()
                .map(category -> mapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }
    
    // SubCategory implementations
    
    @Override
    public SubCategoryDto createSubCategory(SubCategoryDto subCategoryDto, String categoryId) {
        // Find parent category
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
        
        // Check if subcategory already exists in this category
        if (subCategoryRepository.existsBySubCategoryNameIgnoreCaseAndCategoryCategoryId(
                subCategoryDto.getSubCategoryName(), categoryId)) {
            throw new RuntimeException("SubCategory with name '" + subCategoryDto.getSubCategoryName() + 
                    "' already exists in this category");
        }
        
        // Generate unique ID
        String subCategoryId = UUID.randomUUID().toString();
        subCategoryDto.setSubCategoryId(subCategoryId);
        
        // Convert DTO to Entity
        SubCategory subCategory = mapper.map(subCategoryDto, SubCategory.class);
        subCategory.setCategory(category);
        
        // Save subcategory
        SubCategory savedSubCategory = subCategoryRepository.save(subCategory);
        
        // Convert back to DTO
        SubCategoryDto result = mapper.map(savedSubCategory, SubCategoryDto.class);
        result.setCategoryId(categoryId);
        result.setCategoryName(category.getCategoryName());
        
        return result;
    }
    
    @Override
    public SubCategoryDto updateSubCategory(SubCategoryDto subCategoryDto, String subCategoryId) {
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory", "SubCategory Id", subCategoryId));
        
        // Update fields
        subCategory.setSubCategoryName(subCategoryDto.getSubCategoryName());
        subCategory.setDescription(subCategoryDto.getDescription());
        subCategory.setIsActive(subCategoryDto.getIsActive());
        
        // Save updated subcategory
        SubCategory updatedSubCategory = subCategoryRepository.save(subCategory);
        
        SubCategoryDto result = mapper.map(updatedSubCategory, SubCategoryDto.class);
        result.setCategoryId(updatedSubCategory.getCategory().getCategoryId());
        result.setCategoryName(updatedSubCategory.getCategory().getCategoryName());
        
        return result;
    }
    
    @Override
    public void deleteSubCategory(String subCategoryId) {
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory", "SubCategory Id", subCategoryId));
        
        // Soft delete - mark as inactive
        subCategory.setIsActive(false);
        subCategoryRepository.save(subCategory);
    }
    
    @Override
    public SubCategoryDto getSubCategoryById(String subCategoryId) {
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory", "SubCategory Id", subCategoryId));
        
        SubCategoryDto result = mapper.map(subCategory, SubCategoryDto.class);
        result.setCategoryId(subCategory.getCategory().getCategoryId());
        result.setCategoryName(subCategory.getCategory().getCategoryName());
        
        return result;
    }
    
    @Override
    public List<SubCategoryDto> getSubCategoriesByCategory(String categoryId) {
        // Verify category exists
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
        
        List<SubCategory> subCategories = subCategoryRepository.findByCategoryCategoryIdAndIsActiveTrue(categoryId);
        return subCategories.stream()
                .map(subCategory -> {
                    SubCategoryDto dto = mapper.map(subCategory, SubCategoryDto.class);
                    dto.setCategoryId(categoryId);
                    dto.setCategoryName(subCategory.getCategory().getCategoryName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public List<SubCategoryDto> searchSubCategories(String keyword) {
        List<SubCategory> subCategories = subCategoryRepository.findByKeywordAndActive(keyword);
        return subCategories.stream()
                .map(subCategory -> {
                    SubCategoryDto dto = mapper.map(subCategory, SubCategoryDto.class);
                    dto.setCategoryId(subCategory.getCategory().getCategoryId());
                    dto.setCategoryName(subCategory.getCategory().getCategoryName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public List<SubCategoryDto> getAllActiveSubCategories() {
        List<SubCategory> subCategories = subCategoryRepository.findByIsActiveTrue();
        return subCategories.stream()
                .map(subCategory -> {
                    SubCategoryDto dto = mapper.map(subCategory, SubCategoryDto.class);
                    dto.setCategoryId(subCategory.getCategory().getCategoryId());
                    dto.setCategoryName(subCategory.getCategory().getCategoryName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
