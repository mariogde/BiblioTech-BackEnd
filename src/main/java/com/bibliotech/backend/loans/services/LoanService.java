package com.bibliotech.backend.loans.services;

import com.bibliotech.backend.books.models.dtos.BookResponseDTO;
import com.bibliotech.backend.books.models.entities.Book;
import com.bibliotech.backend.books.repositories.BookRepository;
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
        User user = userRepository.findByCpf(dto.getUserCpf()).orElseThrow(() -> new RuntimeException("Usuário não encontrado com o CPF informado"));

        if (Boolean.TRUE.equals(user.getDisabled())) {
            throw new RuntimeException("Não é possível realizar aluguéis para um usuário inativo");
        }

        Book book = bookRepository.findByTitle(dto.getBookTitle()).orElseThrow(() -> new RuntimeException("Livro não encontrado com o título informado"));

        if (book.getInUseQuantity() >= book.getTotalQuantity()) {
            throw new RuntimeException("Não há exemplares disponíveis deste livro para empréstimo");
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
        Loan loan = loanRepository.findById(id).orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

        return toResponseDTO(loan);
    }

    @Transactional
    public LoanResponseDTO returnBook(Long id) {
        Loan loan = loanRepository.findById(id).orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new RuntimeException("Este livro já foi devolvido");
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
        Loan loan = loanRepository.findById(id).orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

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