package com.riwi.librotech.controller.ui;

import com.riwi.librotech.model.Book;
import com.riwi.librotech.service.BookService;
import com.riwi.librotech.service.CategoryService;
import com.riwi.librotech.service.GenreService;
import com.riwi.librotech.service.PublisherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/books")
public class BookUIController {

    private final BookService bookService;
    private final CategoryService categoryService;
    private final PublisherService publisherService;
    private final GenreService genreService;

    public BookUIController(BookService bookService, CategoryService categoryService,
                            PublisherService publisherService, GenreService genreService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.publisherService = publisherService;
        this.genreService = genreService;
    }

    private void populateFormModel(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("publishers", publisherService.findAll());
        model.addAttribute("genres", genreService.findAll());
    }

    @GetMapping
    public String listBooksUI(Model model) {
        model.addAttribute("books", bookService.findAll());
        model.addAttribute("screenTitle", "Book Catalog - Dashboard");
        return "books/list";
    }

    @GetMapping("/new")
    public String showCreationForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("screenTitle", "Register New Book");
        populateFormModel(model);
        return "books/form";
    }

    @PostMapping("/save")
    public String saveBook(@ModelAttribute("book") Book book,
                           @RequestParam(value = "genreIds", required = false) List<Long> genreIds,
                           Model model) {
        int currentYear = LocalDate.now().getYear();
        if (book.getYearPublication() > currentYear) {
            model.addAttribute("yearError",
                    "Publication year cannot be greater than the current year (" + currentYear + ").");
            model.addAttribute("screenTitle", "Register New Book (Correction)");
            populateFormModel(model);
            return "books/form";
        }
        if (genreIds != null) {
            book.setGenres(genreService.findAll().stream()
                    .filter(g -> genreIds.contains(g.getId()))
                    .collect(java.util.stream.Collectors.toList()));
        }
        bookService.save(book);
        return "redirect:/admin/books";
    }
}