package com.riwi.librotech.controller;

import com.riwi.librotech.dto.publisher.PublisherRequestDTO;
import com.riwi.librotech.dto.publisher.PublisherResponseDTO;
import com.riwi.librotech.mapper.PublisherMapper;
import com.riwi.librotech.service.PublisherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {

    private final PublisherService publisherService;
    private final PublisherMapper publisherMapper;

    public PublisherController(PublisherService publisherService, PublisherMapper publisherMapper) {
        this.publisherService = publisherService;
        this.publisherMapper = publisherMapper;
    }

    @GetMapping
    public ResponseEntity<Page<PublisherResponseDTO>> getPublishers(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(publisherService.findAll(pageable).map(publisherMapper::toResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPublisher(@PathVariable Long id) {
        return publisherService.findById(id)
                .<ResponseEntity<?>>map(p -> ResponseEntity.ok(publisherMapper.toResponse(p)))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Publisher not found", "id", id)));
    }

    @PostMapping
    public ResponseEntity<PublisherResponseDTO> createPublisher(@RequestBody PublisherRequestDTO dto) {
        return ResponseEntity.status(201).body(publisherMapper.toResponse(publisherService.save(publisherMapper.toEntity(dto))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePublisher(@PathVariable Long id, @RequestBody PublisherRequestDTO dto) {
        return publisherService.update(id, publisherMapper.toEntity(dto))
                .<ResponseEntity<?>>map(p -> ResponseEntity.ok(publisherMapper.toResponse(p)))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Publisher not found", "id", id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePublisher(@PathVariable Long id) {
        if (publisherService.deleteById(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.status(404).body(Map.of("error", "Publisher not found", "id", id));
    }
}