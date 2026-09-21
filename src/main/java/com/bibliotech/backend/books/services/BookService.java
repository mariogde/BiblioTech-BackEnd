package com.bibliotech.backend.books.services;

import com.bibliotech.backend.books.models.dtos.BookRequestDTO;
import com.bibliotech.backend.books.models.dtos.BookResponseDTO;
import com.bibliotech.backend.books.models.entities.Book;
import com.bibliotech.backend.books.repositories.BookRepository;
import com.bibliotech.backend.publishers.models.dtos.PublisherSummaryDTO;
import com.bibliotech.backend.publishers.models.entities.Publisher;
import com.bibliotech.backend.publishers.repositories.PublisherRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;

    public BookService(BookRepository bookRepository, PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
    }

    @Transactional
    public BookResponseDTO create(BookRequestDTO dto) {
        Publisher publisher = publisherRepository.findById(dto.getPublisherId()).orElseThrow(() -> new RuntimeException("Editora informada não existe"));

        var book = new Book();
        BeanUtils.copyProperties(dto, book);

        book.setInUseQuantity(0);
        book.setPublisher(publisher);

        Book savedBook = bookRepository.save(book);
        return toResponseDTO(savedBook);
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> findAll() {
        return bookRepository.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BookResponseDTO findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        return toResponseDTO(book);
    }

    @Transactional
    public BookResponseDTO update(Long id, BookRequestDTO dto) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        Publisher publisher = publisherRepository.findById(dto.getPublisherId()).orElseThrow(() -> new RuntimeException("Editora informada não existe"));

        BeanUtils.copyProperties(dto, book);
        book.setPublisher(publisher);

        Book updatedBook = bookRepository.save(book);
        return toResponseDTO(updatedBook);
    }

    @Transactional
    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        bookRepository.delete(book);
    }

    private BookResponseDTO toResponseDTO(Book book) {
        BookResponseDTO dto = new BookResponseDTO();
        BeanUtils.copyProperties(book, dto);

        if (book.getPublisher() != null) {
            PublisherSummaryDTO publisherSummary = new PublisherSummaryDTO(
                    book.getPublisher().getId(),
                    book.getPublisher().getName()
            );
            dto.setPublisher(publisherSummary);
        }

        return dto;
    }
}