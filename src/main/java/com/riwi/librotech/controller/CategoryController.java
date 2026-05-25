package com.riwi.librotech.controller;

import com.riwi.librotech.dto.category.CategoryRequestDTO;
import com.riwi.librotech.dto.category.CategoryResponseDTO;
import com.riwi.librotech.mapper.CategoryMapper;
import com.riwi.librotech.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping
    public ResponseEntity<Page<CategoryResponseDTO>> getCategories(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(categoryService.findAll(pageable).map(categoryMapper::toResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategory(@PathVariable Long id) {
        return categoryService.findById(id)
                .<ResponseEntity<?>>map(c -> ResponseEntity.ok(categoryMapper.toResponse(c)))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Category not found", "id", id)));
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CategoryRequestDTO dto) {
        return ResponseEntity.status(201).body(categoryMapper.toResponse(categoryService.save(categoryMapper.toEntity(dto))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryRequestDTO dto) {
        return categoryService.update(id, categoryMapper.toEntity(dto))
                .<ResponseEntity<?>>map(c -> ResponseEntity.ok(categoryMapper.toResponse(c)))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Category not found", "id", id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        if (categoryService.deleteById(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.status(404).body(Map.of("error", "Category not found", "id", id));
    }
}