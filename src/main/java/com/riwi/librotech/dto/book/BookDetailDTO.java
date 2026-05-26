package com.riwi.librotech.dto.book;

import java.util.List;

/**
 * DTO con detalle completo incluyendo géneros.
 * Se construye con post-procesamiento desde la entidad cargada con @EntityGraph,
 * porque JPQL no puede proyectar colecciones directamente en un constructor.
 */
public record BookDetailDTO(
        Long id,
        String title,
        String author,
        String isbn,
        Integer yearPublication,
        String categoryName,
        String publisherName,
        String publisherCountry,
        List<String> genreNames
) {}