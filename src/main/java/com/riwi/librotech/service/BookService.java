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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    // === DTO OPERATIONS (new) ===
    public Optional<BookResponseDTO> getBookById(Long id) {
        return bookRepository.findById(id).map(bookMapper::toResponseDTO);
    }

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

    private void resolveRelations(Book book, BookRequestDTO dto) {
        if (dto.categoryId() != null)
            categoryRepository.findById(dto.categoryId()).ifPresent(book::setCategory);
        if (dto.publisherId() != null)
            publisherRepository.findById(dto.publisherId()).ifPresent(book::setPublisher);
        if (dto.genreIds() != null) {
            book.setGenres(genreRepository.findAllById(dto.genreIds()));
        }
    }

    // === EXISTING METHODS ===

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public boolean deleteById(Long id) {
        return bookRepository.findById(id).map(book -> {
            book.setActive(false);
            book.setDeletedAt(LocalDateTime.now());
            bookRepository.save(book);
            return true;
        }).orElse(false);
    }

    public Page<Book> findAllPaged(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Slice<Book> findAll(Pageable pageable) {
        return bookRepository.findAllBy(pageable);
    }

    public Slice<BookSummaryDTO> getCatalogSlice(int page, int size) {
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        return bookRepository.findAllBookSummariesSlice(PageRequest.of(page, safeSize));
    }

    public Page<BookSummaryDTO> getCatalogPage(int page) {
        return bookRepository.findAllBookSummariesPage(PageRequest.of(page, 10));
    }

    public Book getBookWithRelations(Long id) {
        return bookRepository.findWithRelationsById(id)
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));
    }

    public BookDetailDTO getBookDetail(Long id) {
        Book book = getBookWithRelations(id);
        return new BookDetailDTO(
                book.getId(), book.getTitle(), book.getAuthor(),
                book.getIsbn(), book.getYearPublication(),
                book.getCategory() != null ? book.getCategory().getName() : null,
                book.getPublisher() != null ? book.getPublisher().getName() : null,
                book.getPublisher() != null ? book.getPublisher().getCountry() : null,
                book.getGenres().stream().map(Genre::getName).collect(Collectors.toList())
        );
    }

    public List<BookDetailDTO> getAllBooksDetailJoinFetch() {
        return bookRepository.findAllWithRelationsJPQL().stream()
                .map(book -> new BookDetailDTO(
                        book.getId(), book.getTitle(), book.getAuthor(),
                        book.getIsbn(), book.getYearPublication(),
                        book.getCategory() != null ? book.getCategory().getName() : null,
                        book.getPublisher() != null ? book.getPublisher().getName() : null,
                        book.getPublisher() != null ? book.getPublisher().getCountry() : null,
                        book.getGenres().stream().map(Genre::getName).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    public List<Book> findAllWithRelations() {
        return bookRepository.findAllWithRelations();
    }
}