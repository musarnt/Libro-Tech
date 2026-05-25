package com.riwi.librotech.service;

import com.riwi.librotech.model.Publisher;
import com.riwi.librotech.repository.PublisherRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public Page<Publisher> findAll(Pageable pageable) {
        return publisherRepository.findAll(pageable);
    }

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public List<Publisher> findAll() {
        return publisherRepository.findAll();
    }

    public Optional<Publisher> findById(Long id) {
        return publisherRepository.findById(id);
    }

    public Publisher save(Publisher publisher) {
        if (publisher.getName() == null || publisher.getName().isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        return publisherRepository.save(publisher);
    }

    public Optional<Publisher> update(Long id, Publisher updated) {
        return publisherRepository.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setAddress(updated.getAddress());
            existing.setCountry(updated.getCountry());
            existing.setFoundedIn(updated.getFoundedIn());
            return publisherRepository.save(existing);
        });
    }

    public boolean deleteById(Long id) {
        return publisherRepository.findById(id).map(publisher -> {
            publisher.setDeleted(true);
            publisher.setDeletedAt(LocalDateTime.now());
            publisherRepository.save(publisher);
            return true;
        }).orElse(false);
    }
}