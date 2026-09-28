package com.bibliotech.backend.books.repositories;

import com.bibliotech.backend.books.models.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByPublisherId(Long publisherId);
    Optional<Book> findByTitle(String title);
}