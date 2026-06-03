package com.riwi.librotech.controller;

import com.riwi.librotech.dto.book.BookDetailDTO;
import com.riwi.librotech.dto.book.BookRequestDTO;
import com.riwi.librotech.dto.book.BookResponseDTO;
import com.riwi.librotech.dto.book.BookSummaryDTO;
import com.riwi.librotech.mapper.BookMapper;
import com.riwi.librotech.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    public BookController(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    // GET /api/books?page=0&size=10
    @GetMapping
    public ResponseEntity<Map<String, Object>> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Slice<BookSummaryDTO> slice = bookService.getCatalogSlice(page, size);
        Map<String, Object> response = new HashMap<>();
        response.put("books", slice.getContent());
        response.put("currentPage", slice.getNumber());
        response.put("pageSize", slice.getSize());
        response.put("hasNext", slice.hasNext());
        response.put("hasPrevious", slice.hasPrevious());
        return ResponseEntity.ok(response);
    }

    // GET /api/books/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable Long id) {
        return bookService.findById(id)
                .<ResponseEntity<?>>map(b -> ResponseEntity.ok(bookMapper.toResponseDTO(b)))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Book not found", "id", id)));
    }

    // POST /api/books
    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(@Valid  @RequestBody BookRequestDTO dto) {
        return ResponseEntity.status(201).body(bookService.createBook(dto));
    }

    // PUT /api/books/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody BookRequestDTO dto) {
        return bookService.updateBook(id, dto)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Book not found", "id", id)));
    }

    // DELETE /api/books/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        if (bookService.deleteById(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.status(404).body(Map.of("error", "Book not found", "id", id));
    }

    // GET /api/books/catalog-page?page=0
    @GetMapping("/catalog-page")
    public ResponseEntity<Page<BookSummaryDTO>> getCatalogPage(
            @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(bookService.getCatalogPage(page));
    }

    // GET /api/books/{id}/detail
    @GetMapping("/{id}/detail")
    public ResponseEntity<?> getBookDetail(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(bookService.getBookDetail(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/books/all-detail
    @GetMapping("/all-detail")
    public ResponseEntity<List<BookDetailDTO>> getAllBooksDetail() {
        return ResponseEntity.ok(bookService.getAllBooksDetailJoinFetch());
    }
}