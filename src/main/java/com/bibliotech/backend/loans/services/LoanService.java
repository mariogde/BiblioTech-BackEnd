package com.bibliotech.backend.loans.services;

import com.bibliotech.backend.books.models.dtos.BookResponseDTO;
import com.bibliotech.backend.books.models.entities.Book;
import com.bibliotech.backend.books.repositories.BookRepository;
import com.bibliotech.backend.exceptions.InvalidOperationException;
import com.bibliotech.backend.exceptions.ResourceNotFoundException;
import com.bibliotech.backend.loans.models.dtos.LoanRequestDTO;
import com.bibliotech.backend.loans.models.dtos.LoanResponseDTO;
import com.bibliotech.backend.loans.models.entities.Loan;
import com.bibliotech.backend.loans.models.enums.LoanStatus;
import com.bibliotech.backend.loans.repositories.LoanRepository;
import com.bibliotech.backend.publishers.models.dtos.PublisherSummaryDTO;
import com.bibliotech.backend.users.models.dtos.UserResponseDTO;
import com.bibliotech.backend.users.models.entities.User;
import com.bibliotech.backend.users.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public LoanService(LoanRepository loanRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public LoanResponseDTO create(LoanRequestDTO dto) {
        User user = userRepository.findByCpf(dto.getUserCpf())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with CPF: " + dto.getUserCpf()));

        if (Boolean.TRUE.equals(user.getDisabled())) {
            throw new InvalidOperationException("Cannot create loan for a disabled user account.");
        }

        Book book = bookRepository.findByTitle(dto.getBookTitle())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with title: " + dto.getBookTitle()));

        if (book.getInUseQuantity() >= book.getTotalQuantity()) {
            throw new InvalidOperationException("No available copies of book '" + book.getTitle() + "' for loan.");
        }

        book.setInUseQuantity(book.getInUseQuantity() + 1);
        bookRepository.save(book);

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));
        loan.setStatus(LoanStatus.ACTIVE);

        Loan savedLoan = loanRepository.save(loan);
        return toResponseDTO(savedLoan);
    }

    @Transactional(readOnly = true)
    public List<LoanResponseDTO> findAll() {
        return loanRepository.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LoanResponseDTO findById(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID: " + id));

        return toResponseDTO(loan);
    }

    @Transactional
    public LoanResponseDTO returnBook(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID: " + id));

        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new InvalidOperationException("This book has already been returned.");
        }

        Book book = loan.getBook();
        if (book.getInUseQuantity() > 0) {
            book.setInUseQuantity(book.getInUseQuantity() - 1);
            bookRepository.save(book);
        }

        loan.setReturnDate(LocalDate.now());
        loan.setStatus(LoanStatus.RETURNED);

        Loan updatedLoan = loanRepository.save(loan);
        return toResponseDTO(updatedLoan);
    }

    @Transactional
    public void delete(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID: " + id));

        loanRepository.delete(loan);
    }

    private LoanResponseDTO toResponseDTO(Loan loan) {
        LoanResponseDTO dto = new LoanResponseDTO();
        BeanUtils.copyProperties(loan, dto);

        if (loan.getUser() != null) {
            UserResponseDTO userDTO = new UserResponseDTO();
            BeanUtils.copyProperties(loan.getUser(), userDTO);
            dto.setUser(userDTO);
        }

        if (loan.getBook() != null) {
            BookResponseDTO bookDTO = new BookResponseDTO();
            BeanUtils.copyProperties(loan.getBook(), bookDTO);

            if (loan.getBook().getPublisher() != null) {
                PublisherSummaryDTO publisherSummary = new PublisherSummaryDTO(
                        loan.getBook().getPublisher().getId(),
                        loan.getBook().getPublisher().getName()
                );
                bookDTO.setPublisher(publisherSummary);
            }
            dto.setBook(bookDTO);
        }

        return dto;
    }
}