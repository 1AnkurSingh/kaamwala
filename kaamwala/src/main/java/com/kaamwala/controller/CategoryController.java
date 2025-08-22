package com.kaamwala.controller;

import com.kaamwala.dtos.ApiResponseMessage;
import com.kaamwala.dtos.CategoryDto;
import com.kaamwala.dtos.PageableResponse;
import com.kaamwala.dtos.SubCategoryDto;
import com.kaamwala.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    // ============= CATEGORY ENDPOINTS =============
    
    @PostMapping("/create")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto createdCategory = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }
    
    @PutMapping("/update/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @Valid @RequestBody CategoryDto categoryDto,
            @PathVariable("categoryId") String categoryId) {
        CategoryDto updatedCategory = categoryService.updateCategory(categoryDto, categoryId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable("categoryId") String categoryId) {
        categoryService.deleteCategory(categoryId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("Category deleted successfully!")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    
    @GetMapping("/getById/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("categoryId") String categoryId) {
        CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }
    
    @GetMapping("/getByName/{categoryName}")
    public ResponseEntity<CategoryDto> getCategoryByName(@PathVariable("categoryName") String categoryName) {
        CategoryDto categoryDto = categoryService.getCategoryByName(categoryName);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }
    
    @GetMapping("/getAll")
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "categoryName", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        
        PageableResponse<CategoryDto> categories = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<CategoryDto>> getAllActiveCategories() {
        List<CategoryDto> activeCategories = categoryService.getAllActiveCategories();
        return new ResponseEntity<>(activeCategories, HttpStatus.OK);
    }
    
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<CategoryDto>> searchCategories(@PathVariable("keyword") String keyword) {
        List<CategoryDto> categories = categoryService.searchCategories(keyword);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
    
    // ============= SUBCATEGORY ENDPOINTS =============
    
    @PostMapping("/{categoryId}/subcategories/create")
    public ResponseEntity<SubCategoryDto> createSubCategory(
            @Valid @RequestBody SubCategoryDto subCategoryDto,
            @PathVariable("categoryId") String categoryId) {
        SubCategoryDto createdSubCategory = categoryService.createSubCategory(subCategoryDto, categoryId);
        return new ResponseEntity<>(createdSubCategory, HttpStatus.CREATED);
    }
    
    @PutMapping("/subcategories/update/{subCategoryId}")
    public ResponseEntity<SubCategoryDto> updateSubCategory(
            @Valid @RequestBody SubCategoryDto subCategoryDto,
            @PathVariable("subCategoryId") String subCategoryId) {
        SubCategoryDto updatedSubCategory = categoryService.updateSubCategory(subCategoryDto, subCategoryId);
        return new ResponseEntity<>(updatedSubCategory, HttpStatus.OK);
    }
    
    @DeleteMapping("/subcategories/delete/{subCategoryId}")
    public ResponseEntity<ApiResponseMessage> deleteSubCategory(@PathVariable("subCategoryId") String subCategoryId) {
        categoryService.deleteSubCategory(subCategoryId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("SubCategory deleted successfully!")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    
    @GetMapping("/subcategories/getById/{subCategoryId}")
    public ResponseEntity<SubCategoryDto> getSubCategoryById(@PathVariable("subCategoryId") String subCategoryId) {
        SubCategoryDto subCategoryDto = categoryService.getSubCategoryById(subCategoryId);
        return new ResponseEntity<>(subCategoryDto, HttpStatus.OK);
    }
    
    @GetMapping("/{categoryId}/subcategories")
    public ResponseEntity<List<SubCategoryDto>> getSubCategoriesByCategory(@PathVariable("categoryId") String categoryId) {
        List<SubCategoryDto> subCategories = categoryService.getSubCategoriesByCategory(categoryId);
        return new ResponseEntity<>(subCategories, HttpStatus.OK);
    }
    
    @GetMapping("/subcategories/search/{keyword}")
    public ResponseEntity<List<SubCategoryDto>> searchSubCategories(@PathVariable("keyword") String keyword) {
        List<SubCategoryDto> subCategories = categoryService.searchSubCategories(keyword);
        return new ResponseEntity<>(subCategories, HttpStatus.OK);
    }
    
    @GetMapping("/subcategories/active")
    public ResponseEntity<List<SubCategoryDto>> getAllActiveSubCategories() {
        List<SubCategoryDto> activeSubCategories = categoryService.getAllActiveSubCategories();
        return new ResponseEntity<>(activeSubCategories, HttpStatus.OK);
    }
}
