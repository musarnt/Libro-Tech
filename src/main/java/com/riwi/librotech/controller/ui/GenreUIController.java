package com.riwi.librotech.controller.ui;

import com.riwi.librotech.model.Genre;
import com.riwi.librotech.service.GenreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/genres")
public class GenreUIController {

    private final GenreService genreService;

    public GenreUIController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public String listGenres(Model model) {
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("screenTitle", "Genre Catalog - Dashboard");
        return "genres/list";
    }

    @GetMapping("/new")
    public String showCreationForm(Model model) {
        model.addAttribute("genre", new Genre());
        model.addAttribute("screenTitle", "Register New Genre");
        return "genres/form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        return genreService.findById(id).map(genre -> {
            model.addAttribute("genre", genre);
            model.addAttribute("screenTitle", "Edit Genre");
            return "genres/form";
        }).orElse("redirect:/admin/genres");
    }

    @PostMapping("/save")
    public String saveGenre(@ModelAttribute("genre") Genre genre, Model model) {
        if (genre.getName() == null || genre.getName().isBlank()) {
            model.addAttribute("nameError", "Name cannot be empty.");
            model.addAttribute("screenTitle", genre.getId() == null ? "Register New Genre" : "Edit Genre");
            return "genres/form";
        }
        if (genre.getId() != null) {
            genreService.update(genre.getId(), genre);
        } else {
            genreService.save(genre);
        }
        return "redirect:/admin/genres";
    }

    @PostMapping("/delete/{id}")
    public String deleteGenre(@PathVariable Long id) {
        genreService.deleteById(id);
        return "redirect:/admin/genres";
    }
}