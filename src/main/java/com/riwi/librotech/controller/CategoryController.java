package com.riwi.librotech.controller;

import com.riwi.librotech.model.Category;
import com.riwi.librotech.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<Page<Category>> getCategories(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(categoryService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategory(@PathVariable Long id) {
        return categoryService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404)
                        .body(Map.of("error", "Category not found", "id", id)));
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return ResponseEntity.status(201).body(categoryService.save(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Category updated) {
        return categoryService.update(id, updated)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404)
                        .body(Map.of("error", "Category not found", "id", id)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchCategory(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        return categoryService.findById(id).map(category -> {
                    if (fields.containsKey("name")) category.setName((String) fields.get("name"));
                    if (fields.containsKey("description")) category.setDescription((String) fields.get("description"));
                    return ResponseEntity.ok(categoryService.save(category));
                }).<ResponseEntity<?>>map(r -> r)
                .orElse(ResponseEntity.status(404)
                        .body(Map.of("error", "Category not found", "id", id)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> headCategory(@PathVariable Long id) {
        return categoryService.findById(id).isPresent()
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        if (categoryService.deleteById(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.status(404)
                .body(Map.of("error", "Category not found", "id", id));
    }
}