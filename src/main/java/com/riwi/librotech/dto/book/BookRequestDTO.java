package com.riwi.librotech.dto.book;
import java.util.List;

public record BookRequestDTO(
        String title,
        String author,
        String isbn,
        Integer yearPublication,
        Long categoryId,
        Long publisherId,
        List<Long> genreIds
) {}
