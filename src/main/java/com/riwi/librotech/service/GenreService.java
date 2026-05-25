package com.riwi.librotech.service;

import com.riwi.librotech.model.Genre;
import com.riwi.librotech.repository.GenreRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public Page<Genre> findAll(Pageable pageable) {
        return genreRepository.findAll(pageable);
    }

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    public Optional<Genre> findById(Long id) {
        return genreRepository.findById(id);
    }

    public Genre save(Genre genre) {
        if (genre.getName() == null || genre.getName().isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        return genreRepository.save(genre);
    }

    public Optional<Genre> update(Long id, Genre updated) {
        return genreRepository.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setDescription(updated.getDescription());
            return genreRepository.save(existing);
        });
    }

    public boolean deleteById(Long id) {
        return genreRepository.findById(id).map(genre -> {
            genre.setDeleted(true);
            genre.setDeletedAt(LocalDateTime.now());
            genreRepository.save(genre);
            return true;
        }).orElse(false);
    }
}