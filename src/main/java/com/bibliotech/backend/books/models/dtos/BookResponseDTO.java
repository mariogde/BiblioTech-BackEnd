package com.bibliotech.backend.books.models.dtos;

import com.bibliotech.backend.publishers.models.dtos.PublisherSummaryDTO;
import java.time.LocalDate;

public class BookResponseDTO {

    private Long id;
    private String title;
    private String author;
    private LocalDate releaseDate;
    private Integer totalQuantity;
    private Integer inUseQuantity;
    private PublisherSummaryDTO publisher;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Integer getInUseQuantity() {
        return inUseQuantity;
    }

    public void setInUseQuantity(Integer inUseQuantity) {
        this.inUseQuantity = inUseQuantity;
    }

    public PublisherSummaryDTO getPublisher() {
        return publisher;
    }

    public void setPublisher(PublisherSummaryDTO publisher) {
        this.publisher = publisher;
    }
}