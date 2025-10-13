package com.mssd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mssd.exception.ResourceNotFoundException;
import com.mssd.model.Category;
import com.mssd.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Test Category");
        testCategory.setSlug("test-category");
    }

    @Test
    void getAllCategories_ReturnsList() throws Exception {
        List<Category> categories = Arrays.asList(testCategory);
        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Category"))
                .andExpect(jsonPath("$[0].slug").value("test-category"));

        verify(categoryService).getAllCategories();
    }

    @Test
    void getCategoryById_ValidId_ReturnsCategory() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(testCategory));

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Category"))
                .andExpect(jsonPath("$.slug").value("test-category"));

        verify(categoryService).getCategoryById(1L);
    }

    @Test
    void getCategoryById_InvalidId_ReturnsNotFound() throws Exception {
        when(categoryService.getCategoryById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/categories/999"))
                .andExpect(status().isNotFound());

        verify(categoryService).getCategoryById(999L);
    }

    @Test
    void getCategoryBySlug_ValidSlug_ReturnsCategory() throws Exception {
        when(categoryService.getCategoryBySlug("test-category")).thenReturn(Optional.of(testCategory));

        mockMvc.perform(get("/api/categories/slug/test-category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Category"))
                .andExpect(jsonPath("$.slug").value("test-category"));

        verify(categoryService).getCategoryBySlug("test-category");
    }

    @Test
    void getCategoryBySlug_InvalidSlug_ReturnsNotFound() throws Exception {
        when(categoryService.getCategoryBySlug("invalid-slug")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/categories/slug/invalid-slug"))
                .andExpect(status().isNotFound());

        verify(categoryService).getCategoryBySlug("invalid-slug");
    }

    @Test
    void createCategory_ValidCategory_ReturnsCreated() throws Exception {
        Category newCategory = new Category();
        newCategory.setName("New Category");
        newCategory.setSlug("new-category");

        when(categoryService.createCategory(any(Category.class))).thenReturn(testCategory);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Category"));

        verify(categoryService).createCategory(any(Category.class));
    }

    @Test
    void createCategory_DuplicateSlug_ReturnsBadRequest() throws Exception {
        Category newCategory = new Category();
        newCategory.setName("New Category");
        newCategory.setSlug("existing-slug");

        when(categoryService.createCategory(any(Category.class)))
                .thenThrow(new IllegalArgumentException("Category with slug 'existing-slug' already exists"));

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategory)))
                .andExpect(status().isBadRequest());

        verify(categoryService).createCategory(any(Category.class));
    }

    @Test
    void updateCategory_ValidCategory_ReturnsOk() throws Exception {
        Category updateCategory = new Category();
        updateCategory.setName("Updated Category");
        updateCategory.setSlug("updated-category");

        Category updatedCategory = new Category();
        updatedCategory.setId(1L);
        updatedCategory.setName("Updated Category");
        updatedCategory.setSlug("updated-category");

        when(categoryService.updateCategory(eq(1L), any(Category.class))).thenReturn(updatedCategory);

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Category"))
                .andExpect(jsonPath("$.slug").value("updated-category"));

        verify(categoryService).updateCategory(eq(1L), any(Category.class));
    }

    @Test
    void updateCategory_InvalidId_ReturnsNotFound() throws Exception {
        Category updateCategory = new Category();
        updateCategory.setName("Updated Category");
        updateCategory.setSlug("updated-category");

        when(categoryService.updateCategory(eq(999L), any(Category.class)))
                .thenThrow(new ResourceNotFoundException("Category not found with id: 999"));

        mockMvc.perform(put("/api/categories/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCategory)))
                .andExpect(status().isNotFound());

        verify(categoryService).updateCategory(eq(999L), any(Category.class));
    }

    @Test
    void updateCategory_DuplicateSlug_ReturnsBadRequest() throws Exception {
        Category updateCategory = new Category();
        updateCategory.setName("Updated Category");
        updateCategory.setSlug("existing-slug");

        when(categoryService.updateCategory(eq(1L), any(Category.class)))
                .thenThrow(new IllegalArgumentException("Category with slug 'existing-slug' already exists"));

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCategory)))
                .andExpect(status().isBadRequest());

        verify(categoryService).updateCategory(eq(1L), any(Category.class));
    }

    @Test
    void deleteCategory_ValidId_ReturnsNoContent() throws Exception {
        doNothing().when(categoryService).deleteCategory(1L);

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());

        verify(categoryService).deleteCategory(1L);
    }

    @Test
    void deleteCategory_InvalidId_ReturnsNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Category not found with id: 999"))
                .when(categoryService).deleteCategory(999L);

        mockMvc.perform(delete("/api/categories/999"))
                .andExpect(status().isNotFound());

        verify(categoryService).deleteCategory(999L);
    }

    @Test
    void checkSlugExists_ExistingSlug_ReturnsTrue() throws Exception {
        when(categoryService.existsBySlug("existing-slug")).thenReturn(true);

        mockMvc.perform(get("/api/categories/check/slug/existing-slug"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(categoryService).existsBySlug("existing-slug");
    }

    @Test
    void checkSlugExists_NonExistingSlug_ReturnsFalse() throws Exception {
        when(categoryService.existsBySlug("non-existing-slug")).thenReturn(false);

        mockMvc.perform(get("/api/categories/check/slug/non-existing-slug"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(categoryService).existsBySlug("non-existing-slug");
    }

    @Test
    void checkNameExists_ExistingName_ReturnsTrue() throws Exception {
        when(categoryService.existsByName("Existing Category")).thenReturn(true);

        mockMvc.perform(get("/api/categories/check/name/Existing Category"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(categoryService).existsByName("Existing Category");
    }

    @Test
    void checkNameExists_NonExistingName_ReturnsFalse() throws Exception {
        when(categoryService.existsByName("Non Existing Category")).thenReturn(false);

        mockMvc.perform(get("/api/categories/check/name/Non Existing Category"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(categoryService).existsByName("Non Existing Category");
    }
} 