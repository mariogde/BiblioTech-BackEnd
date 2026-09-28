package com.bibliotech.backend.loans.repositories;

import com.bibliotech.backend.loans.models.entities.Loan;
import com.bibliotech.backend.loans.models.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUserId(Long userId);
    List<Loan> findByStatus(LoanStatus status);
}