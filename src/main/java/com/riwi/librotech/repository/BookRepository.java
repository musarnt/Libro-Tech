package com.riwi.librotech.repository;

import com.riwi.librotech.dto.book.BookSummaryDTO;
import com.riwi.librotech.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Spring genera el SQL automáticamente por el nombre del método
    List<Book> findByAuthor(String author);

    boolean existsByIsbn(String isbn);

    Optional<Book> findByIsbn(String isbn);

    Slice<Book> findAllBy(Pageable pageable);

    // ACTIVIDAD 2 — Proyección JPQL con constructor de Record
    // Una sola query con JOIN, sin N+1, retorna Slice (sin COUNT)
    @Query("""
        SELECT new com.riwi.librotech.dto.book.BookSummaryDTO(
            b.id,
            b.title,
            b.author,
            b.isbn,
            b.yearPublication,
            b.publisher.name,
            b.publisher.country
        )
        FROM Book b
        JOIN b.publisher
        ORDER BY b.yearPublication DESC
        """)
    Slice<BookSummaryDTO> findAllBookSummariesSlice(Pageable pageable);

    // RETO 1 — Misma proyección pero retornando Page (con COUNT)
    // Para comparar en logs: Page genera 2 queries, Slice genera 1
    @Query("""
        SELECT new com.riwi.librotech.dto.book.BookSummaryDTO(
            b.id,
            b.title,
            b.author,
            b.isbn,
            b.yearPublication,
            b.publisher.name,
            b.publisher.country
        )
        FROM Book b
        JOIN b.publisher
        ORDER BY b.yearPublication DESC
        """)
    Page<BookSummaryDTO> findAllBookSummariesPage(Pageable pageable);

    // ACTIVIDAD 3 — @EntityGraph para cargar relaciones sin N+1
    // Para edición/detalle donde necesitamos la entidad completa

    @EntityGraph(attributePaths = {"publisher", "category", "genres"})
    Optional<Book> findWithRelationsById(Long id);

    @EntityGraph(attributePaths = {"publisher", "category", "genres"})
    @Query("SELECT b FROM Book b ORDER BY b.yearPublication DESC")
    List<Book> findAllWithRelations();


    // RETO 3 — JOIN FETCH como alternativa a @EntityGraph
    // Mismo resultado, diferente mecanismo

    @Query("""
        SELECT DISTINCT b FROM Book b
        JOIN FETCH b.publisher
        JOIN FETCH b.category
        JOIN FETCH b.genres
        ORDER BY b.yearPublication DESC
        """)
    List<Book> findAllWithRelationsJPQL();
}