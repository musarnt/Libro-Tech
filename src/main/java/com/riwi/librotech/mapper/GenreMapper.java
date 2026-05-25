package com.riwi.librotech.mapper;

import com.riwi.librotech.dto.genre.GenreRequestDTO;
import com.riwi.librotech.dto.genre.GenreResponseDTO;
import com.riwi.librotech.model.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {

    public GenreResponseDTO toResponse(Genre genre) {
        GenreResponseDTO dto = new GenreResponseDTO();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        dto.setDescription(genre.getDescription());
        return dto;
    }

    public Genre toEntity(GenreRequestDTO dto) {
        Genre genre = new Genre();
        genre.setName(dto.getName());
        genre.setDescription(dto.getDescription());
        return genre;
    }
}
