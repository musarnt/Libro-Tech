// GenreController.java
package com.riwi.librotech.controller;

import com.riwi.librotech.dto.genre.GenreRequestDTO;
import com.riwi.librotech.dto.genre.GenreResponseDTO;
import com.riwi.librotech.mapper.GenreMapper;
import com.riwi.librotech.service.GenreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreService genreService;
    private final GenreMapper genreMapper;

    public GenreController(GenreService genreService, GenreMapper genreMapper) {
        this.genreService = genreService;
        this.genreMapper = genreMapper;
    }

    @GetMapping
    public ResponseEntity<Page<GenreResponseDTO>> getGenres(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(genreService.findAll(pageable).map(genreMapper::toResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGenre(@PathVariable Long id) {
        return genreService.findById(id)
                .<ResponseEntity<?>>map(g -> ResponseEntity.ok(genreMapper.toResponse(g)))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Genre not found", "id", id)));
    }

    @PostMapping
    public ResponseEntity<GenreResponseDTO> createGenre(@RequestBody GenreRequestDTO dto) {
        return ResponseEntity.status(201).body(genreMapper.toResponse(genreService.save(genreMapper.toEntity(dto))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGenre(@PathVariable Long id, @RequestBody GenreRequestDTO dto) {
        return genreService.update(id, genreMapper.toEntity(dto))
                .<ResponseEntity<?>>map(g -> ResponseEntity.ok(genreMapper.toResponse(g)))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Genre not found", "id", id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGenre(@PathVariable Long id) {
        if (genreService.deleteById(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.status(404).body(Map.of("error", "Genre not found", "id", id));
    }
}