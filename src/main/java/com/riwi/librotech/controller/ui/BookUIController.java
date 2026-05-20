package com.riwi.librotech.controller.ui;

import com.riwi.librotech.model.Book;
import com.riwi.librotech.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/books")
public class BookUIController {

    private final BookService bookService;

    public BookUIController(BookService bookService) {
        this.bookService = bookService;
    }

    // GET /admin/books — show book list
    @GetMapping
    public String listBooksUI(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        model.addAttribute("screenTitle", "Book Catalog - Dashboard");
        return "books/list";
    }

    // GET /admin/books/new — show empty form
    @GetMapping("/new")
    public String showCreationForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("screenTitle", "Register New Book");
        return "books/form";
    }

    // POST /admin/books/save — Activity 3 (Lab-7): validate year before saving
    @PostMapping("/save")
    public String saveBook(@ModelAttribute("book") Book book, Model model) {

        int currentYear = LocalDate.now().getYear();

        // Manual business validation
        if (book.getYearPublication() > currentYear) {
            model.addAttribute("yearError",
                    "Publication year cannot be greater than the current year (" + currentYear + ").");
            model.addAttribute("screenTitle", "Register New Book (Correction)");

            // Return the form view (NO redirect) to keep the typed data
            return "books/form";
        }

        // Passes validation — save and redirect (PRG pattern)
        bookService.save(book);
        return "redirect:/admin/books";
    }
}