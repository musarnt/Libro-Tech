package com.riwi.librotech.dto.book;

/**
 * Record liviano para listados masivos.
 * "Aplana" la relación Book → Publisher extrayendo solo los campos necesarios.
 * No tiene proxies de Hibernate, no dispara lazy loading.
 */
public record BookSummaryDTO(
        Long id,
        String title,
        String author,
        String isbn,
        Integer yearPublication,
        String publisherName,
        String publisherCountry
) {}