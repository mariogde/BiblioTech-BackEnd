package com.bibliotech.backend.publishers.controllers;

import com.bibliotech.backend.publishers.models.dtos.PublisherRequestDTO;
import com.bibliotech.backend.publishers.models.dtos.PublisherResponseDTO;
import com.bibliotech.backend.publishers.services.PublisherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping
    public ResponseEntity<PublisherResponseDTO> create(@Valid @RequestBody PublisherRequestDTO dto) {
        PublisherResponseDTO response = publisherService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PublisherResponseDTO>> findAll() {
        return ResponseEntity.ok(publisherService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(publisherService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherResponseDTO> update(@PathVariable Long id, @Valid @RequestBody PublisherRequestDTO dto) {
        PublisherResponseDTO response = publisherService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        publisherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}