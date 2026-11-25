package com.bibliotech.backend.loans.models.dtos;

import com.bibliotech.backend.loans.models.entities.Loan;
import com.bibliotech.backend.loans.models.enums.LoanStatus;
import java.time.LocalDate;

public record TenantLoanHistoryDTO(
        Long id,
        String bookTitle,
        LocalDate loanDate,
        LocalDate dueDate,
        LoanStatus status
) {
    public TenantLoanHistoryDTO(Loan loan) {
        this(
                loan.getId(),
                loan.getBook().getTitle(),
                loan.getLoanDate(),
                loan.getDueDate(),
                loan.getStatus()
        );
    }
}