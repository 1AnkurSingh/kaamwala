package com.kaamwala.service;

import com.kaamwala.dtos.CategoryDto;
import com.kaamwala.dtos.PageableResponse;
import com.kaamwala.dtos.SubCategoryDto;

import java.util.List;

public interface CategoryService {
    
    // Category CRUD operations
    CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto updateCategory(CategoryDto categoryDto, String categoryId);
    void deleteCategory(String categoryId);
    CategoryDto getCategoryById(String categoryId);
    CategoryDto getCategoryByName(String categoryName);
    
    // Get all categories with pagination
    PageableResponse<CategoryDto> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir);
    
    // Get all active categories
    List<CategoryDto> getAllActiveCategories();
    
    // Search categories by keyword
    List<CategoryDto> searchCategories(String keyword);
    
    // SubCategory operations
    SubCategoryDto createSubCategory(SubCategoryDto subCategoryDto, String categoryId);
    SubCategoryDto updateSubCategory(SubCategoryDto subCategoryDto, String subCategoryId);
    void deleteSubCategory(String subCategoryId);
    SubCategoryDto getSubCategoryById(String subCategoryId);
    
    // Get subcategories by category
    List<SubCategoryDto> getSubCategoriesByCategory(String categoryId);
    
    // Search subcategories by keyword
    List<SubCategoryDto> searchSubCategories(String keyword);
    
    // Get all active subcategories
    List<SubCategoryDto> getAllActiveSubCategories();
}
