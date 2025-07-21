package com.bancx.loanpayment.payment.service.impl;

import com.bancx.loanpayment.loan.entity.Loan;
import com.bancx.loanpayment.loan.service.LoanService;
import com.bancx.loanpayment.payment.entity.Payment;
import com.bancx.loanpayment.payment.repository.PaymentRepository;
import com.bancx.loanpayment.payment.rest.PaymentRequest;
import com.bancx.loanpayment.util.LoanStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    private LoanService loanService;
    private PaymentRepository paymentRepository;
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        loanService = mock(LoanService.class);
        paymentRepository = mock(PaymentRepository.class);
        paymentService = new PaymentServiceImpl(loanService, paymentRepository);
    }

    @Test
    void processPayment_shouldReduceBalanceAndReturnPayment() {
        Loan loan = Loan.builder()
                .loanId(1L)
                .loanAmount(new BigDecimal("1000.00"))
                .remainingBalance(new BigDecimal("800.00"))
                .status(LoanStatus.ACTIVE)
                .build();

        PaymentRequest request = PaymentRequest.builder()
                .loanId(1L)
                .paymentAmount(new BigDecimal("300.00"))
                .build();

        when(loanService.getLoan(1L)).thenReturn(loan);
        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.processPayment(request);

        assertEquals(new BigDecimal("300.00"), result.getPaymentAmount());
        assertEquals(1L, result.getLoanId());
        assertNotNull(result.getTimestamp());

        verify(loanService).updateLoan(loan);
        assertEquals(new BigDecimal("500.00"), loan.getRemainingBalance());
        assertEquals(LoanStatus.ACTIVE, loan.getStatus());
    }

    @Test
    void processPayment_shouldSettleLoanIfFullyPaid() {
        Loan loan = Loan.builder()
                .loanId(1L)
                .remainingBalance(new BigDecimal("300.00"))
                .status(LoanStatus.ACTIVE)
                .build();

        PaymentRequest request = PaymentRequest.builder()
                .loanId(1L)
                .paymentAmount(new BigDecimal("300.00"))
                .build();

        when(loanService.getLoan(1L)).thenReturn(loan);
        when(paymentRepository.save(any(Payment.class))).thenReturn(Payment.builder().loanId(1L).paymentAmount(new BigDecimal("300.00")).build());

        paymentService.processPayment(request);

        assertEquals(0, loan.getRemainingBalance().compareTo(BigDecimal.ZERO));
        assertEquals(LoanStatus.SETTLED, loan.getStatus());
    }

    @Test
    void processPayment_shouldThrowIfOverpayment() {
        Loan loan = Loan.builder()
                .loanId(1L)
                .remainingBalance(new BigDecimal("100.00"))
                .status(LoanStatus.ACTIVE)
                .build();

        PaymentRequest request = PaymentRequest.builder()
                .loanId(1L)
                .paymentAmount(new BigDecimal("200.00"))
                .build();

        when(loanService.getLoan(1L)).thenReturn(loan);

        assertThrows(IllegalArgumentException.class, () -> paymentService.processPayment(request));
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void processPayment_shouldThrowIfLoanIsSettled() {
        Loan loan = Loan.builder()
                .loanId(1L)
                .remainingBalance(new BigDecimal("0.00"))
                .status(LoanStatus.SETTLED)
                .build();

        PaymentRequest request = PaymentRequest.builder()
                .loanId(1L)
                .paymentAmount(new BigDecimal("10.00"))
                .build();

        when(loanService.getLoan(1L)).thenReturn(loan);

        assertThrows(IllegalStateException.class, () -> paymentService.processPayment(request));
    }
}
