package com.riwi.librotech.service;

import com.riwi.librotech.model.Book;
import com.riwi.librotech.repository.BookRepository;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import com.riwi.librotech.dto.book.BookSummaryDTO;
import com.riwi.librotech.dto.book.BookDetailDTO;
import org.springframework.data.domain.PageRequest;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private static final int MAX_PAGE_SIZE = 50;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public Book save(Book book) {
        book.setId(null);
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new IllegalArgumentException("Book with this ISBN already exists");
        }
        return bookRepository.save(book);
    }

    public Optional<Book> update(Long id, Book updated) {
        return bookRepository.findById(id).map(existing -> {
            existing.setTitle(updated.getTitle());
            existing.setAuthor(updated.getAuthor());
            existing.setIsbn(updated.getIsbn());
            existing.setYearPublication(updated.getYearPublication());
            return bookRepository.save(existing);
        });
    }

    public boolean deleteById(Long id) {
        return bookRepository.findById(id).map(book -> {
            book.setActive(false);
            book.setDeletedAt(java.time.LocalDateTime.now());
            bookRepository.save(book);
            return true;
        }).orElse(false);
    }

    // REST API (Page con COUNT) — ya existía
    public Page<Book> findAllPaged(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    // UI controller (Slice de entidades) — ya existía
    public Slice<Book> findAll(Pageable pageable) {
        return bookRepository.findAllBy(pageable);
    }


    // Actividad 4 — Catálogo optimizado (Slice de DTOs, 1 sola query)
    public Slice<BookSummaryDTO> getCatalogSlice(int page, int size) {
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page, safeSize);
        return bookRepository.findAllBookSummariesSlice(pageable);
    }

    // Reto 1 — Mismo catálogo con Page (2 queries, para comparar logs)
    public Page<BookSummaryDTO> getCatalogPage(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return bookRepository.findAllBookSummariesPage(pageable);
    }

    // Actividad 3 — Libro con relaciones cargadas (@EntityGraph)
    public Book getBookWithRelations(Long id) {
        return bookRepository.findWithRelationsById(id)
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));
    }

    // Reto 2 — Detalle completo con géneros (post-procesamiento)
    public BookDetailDTO getBookDetail(Long id) {
        Book book = getBookWithRelations(id);
        return new BookDetailDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getYearPublication(),
                book.getCategory() != null ? book.getCategory().getName() : null,
                book.getPublisher() != null ? book.getPublisher().getName() : null,
                book.getPublisher() != null ? book.getPublisher().getCountry() : null,
                book.getGenres().stream()
                        .map(g -> g.getName())
                        .collect(Collectors.toList())
        );
    }

    public List<BookDetailDTO> getAllBooksDetailJoinFetch() {
        return bookRepository.findAllWithRelationsJPQL().stream()
                .map(book -> new BookDetailDTO(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getIsbn(),
                        book.getYearPublication(),
                        book.getCategory() != null ? book.getCategory().getName() : null,
                        book.getPublisher() != null ? book.getPublisher().getName() : null,
                        book.getPublisher() != null ? book.getPublisher().getCountry() : null,
                        book.getGenres().stream()
                                .map(g -> g.getName())
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
}