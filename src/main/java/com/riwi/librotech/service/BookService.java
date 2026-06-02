package com.riwi.librotech.service;

import com.riwi.librotech.dto.book.BookDetailDTO;
import com.riwi.librotech.dto.book.BookRequestDTO;
import com.riwi.librotech.dto.book.BookResponseDTO;
import com.riwi.librotech.dto.book.BookSummaryDTO;
import com.riwi.librotech.mapper.BookMapper;
import com.riwi.librotech.model.Book;
import com.riwi.librotech.model.Genre;
import com.riwi.librotech.repository.BookRepository;
import com.riwi.librotech.repository.CategoryRepository;
import com.riwi.librotech.repository.GenreRepository;
import com.riwi.librotech.repository.PublisherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final GenreRepository genreRepository;
    private static final int MAX_PAGE_SIZE = 50;

    public BookService(BookRepository bookRepository, BookMapper bookMapper,
                       CategoryRepository categoryRepository,
                       PublisherRepository publisherRepository,
                       GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
        this.genreRepository = genreRepository;
    }

    // === DTO-BASED CRUD ===

    // Returns a single book as ResponseDTO
    public Optional<BookResponseDTO> getBookById(Long id) {
        return bookRepository.findById(id).map(bookMapper::toResponseDTO);
    }

    // Creates a book from RequestDTO, resolves relations by ID
    @Transactional
    public BookResponseDTO createBook(BookRequestDTO dto) {
        Book book = bookMapper.toEntity(dto);

        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new IllegalArgumentException("Book with this ISBN already exists");
        }

        resolveRelations(book, dto);
        return bookMapper.toResponseDTO(bookRepository.save(book));
    }

    // Updates an existing book, returns empty if not found
    @Transactional
    public Optional<BookResponseDTO> updateBook(Long id, BookRequestDTO dto) {
        return bookRepository.findById(id).map(book -> {
            book.setTitle(dto.title());
            book.setAuthor(dto.author());
            book.setIsbn(dto.isbn());
            book.setYearPublication(dto.yearPublication());
            resolveRelations(book, dto);
            return bookMapper.toResponseDTO(bookRepository.save(book));
        });
    }

    // Soft delete: sets active=false instead of removing
    public boolean deleteById(Long id) {
        return bookRepository.findById(id).map(book -> {
            book.setActive(false);
            book.setDeletedAt(LocalDateTime.now());
            bookRepository.save(book);
            return true;
        }).orElse(false);
    }

    // === CATALOG QUERIES ===

    // Lightweight DTO slice for REST API (1 query, no COUNT)
    public Slice<BookSummaryDTO> getCatalogSlice(int page, int size) {
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        return bookRepository.findAllBookSummariesSlice(PageRequest.of(page, safeSize));
    }

    // Same catalog with Page (2 queries, includes total count)
    public Page<BookSummaryDTO> getCatalogPage(int page) {
        return bookRepository.findAllBookSummariesPage(PageRequest.of(page, 10));
    }

    // === DETAIL QUERIES ===

    // Full detail with genres for a single book
    public BookDetailDTO getBookDetail(Long id) {
        Book book = getBookWithRelations(id);
        return toDetailDTO(book);
    }

    // Full detail for all books (JOIN FETCH, no N+1)
    public List<BookDetailDTO> getAllBooksDetailJoinFetch() {
        return bookRepository.findAllWithRelationsJPQL().stream()
                .map(this::toDetailDTO)
                .toList();
    }

    // === ENTITY ACCESS (used by UI layer and internal queries) ===

    // Returns raw entity — used by BookUIController for edit form
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Page<Book> findAllPaged(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Slice<Book> findAll(Pageable pageable) {
        return bookRepository.findAllBy(pageable);
    }

    public List<Book> findAllWithRelations() {
        return bookRepository.findAllWithRelations();
    }

    // === PRIVATE HELPERS ===

    // Resolves category, publisher, and genres from their IDs
    private void resolveRelations(Book book, BookRequestDTO dto) {
        if (dto.categoryId() != null) {
            book.setCategory(categoryRepository.findById(dto.categoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found: " + dto.categoryId())));
        }
        if (dto.publisherId() != null) {
            book.setPublisher(publisherRepository.findById(dto.publisherId())
                    .orElseThrow(() -> new RuntimeException("Publisher not found: " + dto.publisherId())));
        }
        if (dto.genreIds() != null) {
            book.setGenres(new HashSet<>(genreRepository.findAllById(dto.genreIds())));
        }
    }

    // Loads a book with all relations via @EntityGraph
    private Book getBookWithRelations(Long id) {
        return bookRepository.findWithRelationsById(id)
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));
    }

    // Converts entity to BookDetailDTO (manual, genres need post-processing)
    private BookDetailDTO toDetailDTO(Book book) {
        return new BookDetailDTO(
                book.getId(), book.getTitle(), book.getAuthor(),
                book.getIsbn(), book.getYearPublication(),
                book.getCategory() != null ? book.getCategory().getName() : null,
                book.getPublisher() != null ? book.getPublisher().getName() : null,
                book.getPublisher() != null ? book.getPublisher().getCountry() : null,
                book.getGenres().stream().map(Genre::getName).toList()
        );
    }
}