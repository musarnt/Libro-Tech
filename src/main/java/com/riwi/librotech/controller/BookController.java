package com.riwi.librotech.controller;

import com.riwi.librotech.model.Book;
import com.riwi.librotech.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getBooks() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable Long id) {
        return bookService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404)
                        .body(Map.of("error", "Book not found", "id", id)));
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        return ResponseEntity.status(201).body(bookService.save(book));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody Book updated) {
        return bookService.update(id, updated)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404)
                        .body(Map.of("error", "Book not found", "id", id)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchBook(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        return bookService.findById(id).map(book -> {
                    if (fields.containsKey("title")) book.setTitle((String) fields.get("title"));
                    if (fields.containsKey("author")) book.setAuthor((String) fields.get("author"));
                    if (fields.containsKey("isbn")) book.setIsbn((String) fields.get("isbn"));
                    if (fields.containsKey("yearPublication")) book.setYearPublication((Integer) fields.get("yearPublication"));
                    return ResponseEntity.ok(bookService.save(book));
                }).<ResponseEntity<?>>map(r -> r)
                .orElse(ResponseEntity.status(404)
                        .body(Map.of("error", "Book not found", "id", id)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> headBook(@PathVariable Long id) {
        return bookService.findById(id).isPresent()
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build(); // HEAD no lleva body por especificación HTTP
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        if (bookService.deleteById(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.status(404)
                .body(Map.of("error", "Book not found", "id", id));
    }
}