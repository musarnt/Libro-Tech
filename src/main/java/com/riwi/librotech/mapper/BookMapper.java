package com.riwi.librotech.mapper;

import com.riwi.librotech.dto.book.BookRequestDTO;
import com.riwi.librotech.dto.book.BookResponseDTO;
import com.riwi.librotech.model.Book;
import com.riwi.librotech.model.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface BookMapper {

    // === ENTIDAD A RESPONSE DTO ===
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "publisher.name", target = "publisherName")
    @Mapping(source = "genres", target = "genreNames", qualifiedByName = "mapGenreNames")
    BookResponseDTO toResponseDTO(Book book);

    @Named("mapGenreNames")
    default List<String> mapGenreNames(Set<Genre> genres){
        if (genres == null) return List.of();
        return genres.stream()
                .map(Genre::getName)
                .toList();
    }

    // === REQUEST DTO A ENTIDAD ===
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "publisher", ignore = true)
    @Mapping(target = "genres", ignore = true)
    Book toEntity(BookRequestDTO dto);
}