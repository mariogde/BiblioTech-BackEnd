package com.bibliotech.backend.loans.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoanRequestDTO {

    @NotBlank
    @Size(min = 11, max = 14)
    private String userCpf;

    @NotBlank
    private String bookTitle;

    public LoanRequestDTO() {
    }
    public String getUserCpf() {
        return userCpf;
    }
    public void setUserCpf(String userCpf) {
        this.userCpf = userCpf;
    }
    public String getBookTitle() {
        return bookTitle;
    }
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
}