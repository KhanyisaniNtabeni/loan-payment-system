package com.bancx.loanpayment.loan.repository;

import com.bancx.exception.LoanNotFoundException;
import com.bancx.loanpayment.loan.entity.Loan;
import com.bancx.loanpayment.loan.repoaitory.LoanRepository;
import com.bancx.loanpayment.loan.rest.LoanRequest;

import com.bancx.loanpayment.loan.service.impl.LoanServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class LoanServiceIntegrationTest {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanServiceImpl loanService;

    @Test
    void createLoan_shouldPersistAndRetrieveLoan() {
        LoanRequest request = LoanRequest.builder()
                .loanAmount(new BigDecimal("5000.00"))
                .term(24)
                .build();

        Loan createdLoan = loanService.createLoan(request);

        assertNotNull(createdLoan.getLoanId());

        Loan fromDb = loanService.getLoan(createdLoan.getLoanId());

        assertEquals(new BigDecimal("5000.00"), fromDb.getLoanAmount());
        assertEquals(24, fromDb.getTerm());
        assertEquals(createdLoan.getLoanId(), fromDb.getLoanId());
        assertEquals(createdLoan.getRemainingBalance(), fromDb.getRemainingBalance());
        assertEquals(createdLoan.getStatus(), fromDb.getStatus());
    }

    @Test
    void getLoan_shouldThrowWhenLoanNotFound() {
        assertThrows(LoanNotFoundException.class, () -> loanService.getLoan(9999L));
    }

    @Test
    void updateLoan_shouldUpdatePersistedLoan() {
        LoanRequest request = LoanRequest.builder()
                .loanAmount(new BigDecimal("1000.00"))
                .term(12)
                .build();

        Loan loan = loanService.createLoan(request);

        loan.setLoanAmount(new BigDecimal("2000.00"));
        loanService.updateLoan(loan);

        Loan updatedLoan = loanService.getLoan(loan.getLoanId());

        assertEquals(new BigDecimal("2000.00"), updatedLoan.getLoanAmount());
    }
}
