package com.mssd.service.impl;

import com.mssd.exception.ResourceNotFoundException;
import com.mssd.model.Category;
import com.mssd.repository.CategoryRepository;
import com.mssd.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    @Override
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }
    
    @Override
    public Optional<Category> getCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }
    
    @Override
    public Category createCategory(Category category) {
        // Validate that slug and name are unique
        if (categoryRepository.existsBySlug(category.getSlug())) {
            throw new IllegalArgumentException("Category with slug '" + category.getSlug() + "' already exists");
        }
        
        if (categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category with name '" + category.getName() + "' already exists");
        }
        
        return categoryRepository.save(category);
    }
    
    @Override
    public Category updateCategory(Long id, Category category) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        
        // Check if the new slug conflicts with another category (excluding current one)
        if (!existingCategory.getSlug().equals(category.getSlug()) && 
            categoryRepository.existsBySlug(category.getSlug())) {
            throw new IllegalArgumentException("Category with slug '" + category.getSlug() + "' already exists");
        }
        
        // Check if the new name conflicts with another category (excluding current one)
        if (!existingCategory.getName().equals(category.getName()) && 
            categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category with name '" + category.getName() + "' already exists");
        }
        
        existingCategory.setName(category.getName());
        existingCategory.setSlug(category.getSlug());
        
        return categoryRepository.save(existingCategory);
    }
    
    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
    
    @Override
    public boolean existsBySlug(String slug) {
        return categoryRepository.existsBySlug(slug);
    }
    
    @Override
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }
} 