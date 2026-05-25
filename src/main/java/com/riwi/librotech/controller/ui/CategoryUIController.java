package com.riwi.librotech.controller.ui;

import com.riwi.librotech.model.Category;
import com.riwi.librotech.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/categories")
public class CategoryUIController {

    private final CategoryService categoryService;

    public CategoryUIController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("screenTitle", "Category Catalog - Dashboard");
        return "categories/list";
    }

    @GetMapping("/new")
    public String showCreationForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("screenTitle", "Register New Category");
        return "categories/form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        return categoryService.findById(id).map(category -> {
            model.addAttribute("category", category);
            model.addAttribute("screenTitle", "Edit Category");
            return "categories/form";
        }).orElse("redirect:/admin/categories");
    }

    @PostMapping("/save")
    public String saveCategory(@ModelAttribute("category") Category category, Model model) {
        if (category.getName() == null || category.getName().isBlank()) {
            model.addAttribute("nameError", "Name cannot be empty.");
            model.addAttribute("screenTitle", category.getId() == null ? "Register New Category" : "Edit Category");
            return "categories/form";
        }
        if (category.getId() != null) {
            categoryService.update(category.getId(), category);
        } else {
            categoryService.save(category);
        }
        return "redirect:/admin/categories";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
        return "redirect:/admin/categories";
    }
}