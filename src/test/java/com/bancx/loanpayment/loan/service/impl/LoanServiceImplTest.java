package com.bancx.loanpayment.loan.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.bancx.loanpayment.exception.LoanNotFoundException;
import com.bancx.loanpayment.loan.entity.Loan;
import com.bancx.loanpayment.loan.repoaitory.LoanRepository;
import com.bancx.loanpayment.loan.rest.LoanRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Optional;

class LoanServiceImplTest {

    private LoanRepository loanRepository;
    private LoanServiceImpl loanService;

    @BeforeEach
    void setUp() {
        loanRepository = mock(LoanRepository.class);
        loanService = new LoanServiceImpl(loanRepository);
    }

    @Test
    void createLoan_shouldSaveAndReturnLoan() {
        LoanRequest request = LoanRequest.builder()
                .loanAmount(new BigDecimal("1000.00"))
                .term(12)
                .build();

        Loan savedLoan = Loan.builder()
                .loanAmount(request.getLoanAmount())
                .term(request.getTerm())
                .loanId(1L)
                .build();

        when(loanRepository.save(any(Loan.class))).thenReturn(savedLoan);

        Loan result = loanService.createLoan(request);

        assertNotNull(result);
        assertEquals(1L, result.getLoanId());
        assertEquals(new BigDecimal("1000.00"), result.getLoanAmount());
        assertEquals(12, result.getTerm());

        ArgumentCaptor<Loan> loanCaptor = ArgumentCaptor.forClass(Loan.class);
        verify(loanRepository).save(loanCaptor.capture());
        Loan captured = loanCaptor.getValue();

        assertEquals(request.getLoanAmount(), captured.getLoanAmount());
        assertEquals(request.getTerm(), captured.getTerm());
    }

    @Test
    void getLoan_shouldReturnLoanIfExists() {
        Loan loan = Loan.builder().loanId(1L).build();

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Loan found = loanService.getLoan(1L);

        assertEquals(1L, found.getLoanId());
    }

    @Test
    void getLoan_shouldThrowExceptionIfNotFound() {
        when(loanRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LoanNotFoundException.class, () -> loanService.getLoan(1L));
    }

    @Test
    void updateLoan_shouldSaveLoan() {
        Loan loan = Loan.builder().loanId(1L).build();

        loanService.updateLoan(loan);

        verify(loanRepository).save(loan);
    }
}
