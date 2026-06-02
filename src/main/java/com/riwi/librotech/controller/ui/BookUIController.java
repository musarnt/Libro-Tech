package com.riwi.librotech.controller.ui;

import com.riwi.librotech.dto.book.BookFormDTO;
import com.riwi.librotech.dto.book.BookRequestDTO;
import com.riwi.librotech.dto.book.BookSummaryDTO;
import com.riwi.librotech.service.BookService;
import com.riwi.librotech.service.CategoryService;
import com.riwi.librotech.service.GenreService;
import com.riwi.librotech.service.PublisherService;
import org.springframework.data.domain.Slice;
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
    public String listBooksUI(@RequestParam(defaultValue = "0") int page, Model model) {
        Slice<BookSummaryDTO> booksSlice = bookService.getCatalogSlice(page, 10);
        model.addAttribute("books", booksSlice.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("hasNext", booksSlice.hasNext());
        model.addAttribute("hasPrevious", booksSlice.hasPrevious());
        return "books/list";
    }

    @GetMapping("/new")
    public String showCreationForm(Model model) {
        model.addAttribute("book", new BookFormDTO());
        model.addAttribute("screenTitle", "Register New Book");
        populateFormModel(model);
        return "books/form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        return bookService.findById(id).map(book -> {
            BookFormDTO form = new BookFormDTO(
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    book.getYearPublication(),
                    book.getCategory() != null ? book.getCategory().getId() : null,
                    book.getPublisher() != null ? book.getPublisher().getId() : null,
                    book.getGenres() != null
                            ? book.getGenres().stream().map(g -> g.getId()).toList()
                            : List.of()
            );
            model.addAttribute("book", form);
            model.addAttribute("screenTitle", "Edit Book");
            populateFormModel(model);
            return "books/form";
        }).orElse("redirect:/admin/books");
    }

    @PostMapping("/save")
    public String saveBook(@ModelAttribute("book") BookFormDTO form, Model model) {
        int currentYear = LocalDate.now().getYear();
        if (form.getYearPublication() != null && form.getYearPublication() > currentYear) {
            model.addAttribute("yearError",
                    "Publication year cannot be greater than the current year (" + currentYear + ").");
            model.addAttribute("screenTitle", form.getId() == null ? "Register New Book" : "Edit Book");
            populateFormModel(model);
            return "books/form";
        }

        BookRequestDTO dto = new BookRequestDTO(
                form.getTitle(),
                form.getAuthor(),
                form.getIsbn(),
                form.getYearPublication(),
                form.getCategoryId(),
                form.getPublisherId(),
                form.getGenreIds()
        );

        if (form.getId() != null) {
            bookService.updateBook(form.getId(), dto);
        } else {
            bookService.createBook(dto);
        }
        return "redirect:/admin/books";
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return "redirect:/admin/books";
    }
}