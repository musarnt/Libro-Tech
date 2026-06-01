package com.riwi.librotech.dto.book;
import java.util.List;

public record BookResponseDTO(
        Long id,
        String title,
        String author,
        String isbn,
        Integer yearPublication,
        String categoryName,
        String publisherName,
        List<String> genreNames
) {}