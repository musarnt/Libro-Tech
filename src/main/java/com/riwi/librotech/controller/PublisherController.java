package com.riwi.librotech.controller;

import com.riwi.librotech.model.Publisher;
import com.riwi.librotech.service.PublisherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import java.util.Map;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public ResponseEntity<Page<Publisher>> getPublishers(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(publisherService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPublisher(@PathVariable Long id) {
        return publisherService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404)
                        .body(Map.of("error", "Publisher not found", "id", id)));
    }

    @PostMapping
    public ResponseEntity<Publisher> createPublisher(@RequestBody Publisher publisher) {
        return ResponseEntity.status(201).body(publisherService.save(publisher));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePublisher(@PathVariable Long id, @RequestBody Publisher updated) {
        return publisherService.update(id, updated)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404)
                        .body(Map.of("error", "Publisher not found", "id", id)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchPublisher(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        return publisherService.findById(id).map(publisher -> {
                    if (fields.containsKey("name")) publisher.setName((String) fields.get("name"));
                    if (fields.containsKey("address")) publisher.setAddress((String) fields.get("address"));
                    if (fields.containsKey("country")) publisher.setCountry((String) fields.get("country"));
                    if (fields.containsKey("foundedIn")) publisher.setFoundedIn((Integer) fields.get("foundedIn"));
                    return ResponseEntity.ok(publisherService.save(publisher));
                }).<ResponseEntity<?>>map(r -> r)
                .orElse(ResponseEntity.status(404)
                        .body(Map.of("error", "Publisher not found", "id", id)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> headPublisher(@PathVariable Long id) {
        return publisherService.findById(id).isPresent()
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePublisher(@PathVariable Long id) {
        if (publisherService.deleteById(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.status(404)
                .body(Map.of("error", "Publisher not found", "id", id));
    }
}