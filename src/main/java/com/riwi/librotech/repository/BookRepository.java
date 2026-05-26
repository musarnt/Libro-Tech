package com.riwi.librotech.repository;

import com.riwi.librotech.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
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
}