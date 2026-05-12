package com.riwi.librotech.controller;

import com.riwi.librotech.model.Book;
import com.riwi.librotech.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getBooks() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        return ResponseEntity.status(201).body(bookService.save(book));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book updated) {
        return bookService.update(id, updated)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Book> patchBook(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        return bookService.findById(id).map(book -> {
            if (fields.containsKey("title")) book.setTitle((String) fields.get("title"));
            if (fields.containsKey("author")) book.setAuthor((String) fields.get("author"));
            if (fields.containsKey("isbn")) book.setIsbn((String) fields.get("isbn"));
            if (fields.containsKey("yearPublication")) book.setYearPublication((Integer) fields.get("yearPublication"));
            return ResponseEntity.ok(bookService.save(book));
        }).orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> headBook(@PathVariable Long id) {
        return bookService.findById(id).isPresent()
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        return bookService.deleteById(id)
                ? ResponseEntity.ok("Book deleted")
                : ResponseEntity.notFound().build();
    }
}