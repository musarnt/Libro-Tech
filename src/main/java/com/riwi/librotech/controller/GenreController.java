package com.riwi.librotech.controller;

import com.riwi.librotech.model.Genre;
import com.riwi.librotech.service.GenreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import java.util.Map;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public ResponseEntity<Page<Genre>> getGenres(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(genreService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGenre(@PathVariable Long id) {
        return genreService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404)
                        .body(Map.of("error", "Genre not found", "id", id)));
    }

    @PostMapping
    public ResponseEntity<Genre> createGenre(@RequestBody Genre genre) {
        return ResponseEntity.status(201).body(genreService.save(genre));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGenre(@PathVariable Long id, @RequestBody Genre updated) {
        return genreService.update(id, updated)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404)
                        .body(Map.of("error", "Genre not found", "id", id)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchGenre(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        return genreService.findById(id).map(genre -> {
                    if (fields.containsKey("name")) genre.setName((String) fields.get("name"));
                    if (fields.containsKey("description")) genre.setDescription((String) fields.get("description"));
                    return ResponseEntity.ok(genreService.save(genre));
                }).<ResponseEntity<?>>map(r -> r)
                .orElse(ResponseEntity.status(404)
                        .body(Map.of("error", "Genre not found", "id", id)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> headGenre(@PathVariable Long id) {
        return genreService.findById(id).isPresent()
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGenre(@PathVariable Long id) {
        if (genreService.deleteById(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.status(404)
                .body(Map.of("error", "Genre not found", "id", id));
    }
}