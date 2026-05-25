package com.riwi.librotech.mapper;

import com.riwi.librotech.dto.book.BookRequestDTO;
import com.riwi.librotech.dto.book.BookResponseDTO;
import com.riwi.librotech.model.Book;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    public BookResponseDTO toResponse(Book book) {
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setIsbn(book.getIsbn());
        dto.setYearPublication(book.getYearPublication());
        if (book.getCategory() != null)
            dto.setCategoryName(book.getCategory().getName());
        if (book.getPublisher() != null)
            dto.setPublisherName(book.getPublisher().getName());
        if (book.getGenres() != null)
            dto.setGenreNames(book.getGenres().stream()
                    .map(g -> g.getName())
                    .collect(Collectors.toList()));
        return dto;
    }
}
