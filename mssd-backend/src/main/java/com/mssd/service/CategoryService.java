package com.mssd.service;

import com.mssd.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    
    List<Category> getAllCategories();
    
    Optional<Category> getCategoryById(Long id);
    
    Optional<Category> getCategoryBySlug(String slug);
    
    Category createCategory(Category category);
    
    Category updateCategory(Long id, Category category);
    
    void deleteCategory(Long id);
    
    boolean existsBySlug(String slug);
    
    boolean existsByName(String name);
} 