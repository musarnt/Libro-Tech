// BookController.java
package com.riwi.librotech.controller;

import com.riwi.librotech.dto.book.BookRequestDTO;
import com.riwi.librotech.dto.book.BookResponseDTO;
import com.riwi.librotech.mapper.BookMapper;
import com.riwi.librotech.model.Book;
import com.riwi.librotech.model.Category;
import com.riwi.librotech.model.Genre;
import com.riwi.librotech.model.Publisher;
import com.riwi.librotech.repository.CategoryRepository;
import com.riwi.librotech.repository.GenreRepository;
import com.riwi.librotech.repository.PublisherRepository;
import com.riwi.librotech.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final GenreRepository genreRepository;

    public BookController(BookService bookService, BookMapper bookMapper,
                          CategoryRepository categoryRepository,
                          PublisherRepository publisherRepository,
                          GenreRepository genreRepository) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
        this.genreRepository = genreRepository;
    }

    @GetMapping
    public ResponseEntity<Page<BookResponseDTO>> getBooks(
            @PageableDefault(size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(bookService.findAllPaged(pageable).map(bookMapper::toResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable Long id) {
        return bookService.findById(id)
                .<ResponseEntity<?>>map(b -> ResponseEntity.ok(bookMapper.toResponse(b)))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Book not found", "id", id)));
    }

    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(@RequestBody BookRequestDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setIsbn(dto.getIsbn());
        book.setYearPublication(dto.getYearPublication());

        if (dto.getCategoryId() != null)
            categoryRepository.findById(dto.getCategoryId()).ifPresent(book::setCategory);
        if (dto.getPublisherId() != null)
            publisherRepository.findById(dto.getPublisherId()).ifPresent(book::setPublisher);
        if (dto.getGenreIds() != null) {
            List<Genre> genres = genreRepository.findAllById(dto.getGenreIds());
            book.setGenres(genres);
        }

        return ResponseEntity.status(201).body(bookMapper.toResponse(bookService.save(book)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody BookRequestDTO dto) {
        return bookService.findById(id).map(book -> {
                    book.setTitle(dto.getTitle());
                    book.setAuthor(dto.getAuthor());
                    book.setIsbn(dto.getIsbn());
                    book.setYearPublication(dto.getYearPublication());

                    if (dto.getCategoryId() != null)
                        categoryRepository.findById(dto.getCategoryId()).ifPresent(book::setCategory);
                    if (dto.getPublisherId() != null)
                        publisherRepository.findById(dto.getPublisherId()).ifPresent(book::setPublisher);
                    if (dto.getGenreIds() != null) {
                        List<Genre> genres = genreRepository.findAllById(dto.getGenreIds());
                        book.setGenres(genres);
                    }

                    return ResponseEntity.ok(bookMapper.toResponse(bookService.save(book)));
                }).<ResponseEntity<?>>map(r -> r)
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Book not found", "id", id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        if (bookService.deleteById(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.status(404).body(Map.of("error", "Book not found", "id", id));
    }
}