package com.bibliotech.backend.publishers.services;

import com.bibliotech.backend.books.repositories.BookRepository;
import com.bibliotech.backend.publishers.models.dtos.PublisherRequestDTO;
import com.bibliotech.backend.publishers.models.dtos.PublisherResponseDTO;
import com.bibliotech.backend.publishers.models.entities.Publisher;
import com.bibliotech.backend.publishers.repositories.PublisherRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;

    public PublisherService(PublisherRepository publisherRepository, BookRepository bookRepository) {
        this.publisherRepository = publisherRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public PublisherResponseDTO create(PublisherRequestDTO dto) {
        if (publisherRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("E-mail de editora já cadastrado");
        }

        var publisher = new Publisher();
        BeanUtils.copyProperties(dto, publisher);

        Publisher savedPublisher = publisherRepository.save(publisher);
        return toResponseDTO(savedPublisher);
    }

    @Transactional(readOnly = true)
    public List<PublisherResponseDTO> findAll() {
        return publisherRepository.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PublisherResponseDTO findById(Long id) {
        Publisher publisher = publisherRepository.findById(id).orElseThrow(() -> new RuntimeException("Editora não encontrada"));

        return toResponseDTO(publisher);
    }

    @Transactional
    public PublisherResponseDTO update(Long id, PublisherRequestDTO dto) {
        Publisher publisher = publisherRepository.findById(id).orElseThrow(() -> new RuntimeException("Editora não encontrada"));

        BeanUtils.copyProperties(dto, publisher);

        Publisher updatedPublisher = publisherRepository.save(publisher);
        return toResponseDTO(updatedPublisher);
    }

    @Transactional
    public void delete(Long id) {
        Publisher publisher = publisherRepository.findById(id).orElseThrow(() -> new RuntimeException("Editora não encontrada"));

        publisherRepository.delete(publisher);
    }

    private PublisherResponseDTO toResponseDTO(Publisher publisher) {
        PublisherResponseDTO dto = new PublisherResponseDTO();
        BeanUtils.copyProperties(publisher, dto);
        return dto;
    }
}
