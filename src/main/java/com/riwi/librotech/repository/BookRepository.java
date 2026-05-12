package com.riwi.librotech.repository;

import com.riwi.librotech.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // Spring genera el SQL automáticamente por el nombre del método
    List<Book> findByAuthor(String author);
}