package com.bibliotech.backend.publishers.repositories;

import com.bibliotech.backend.publishers.models.entities.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    boolean existsByEmail(String email);
}