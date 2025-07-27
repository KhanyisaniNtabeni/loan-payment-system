package com.bancx.loanpayment.payment.service.impl;

import com.bancx.loanpayment.loan.entity.Loan;
import com.bancx.loanpayment.loan.service.LoanService;
import com.bancx.loanpayment.payment.entity.Payment;
import com.bancx.loanpayment.payment.repository.PaymentRepository;
import com.bancx.loanpayment.payment.rest.PaymentRequest;
import com.bancx.loanpayment.payment.service.PaymentService;
import com.bancx.loanpayment.util.LoanStatus;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service that handles business logic for loan payments,
 * including validation, balance update, and loan settlement.
 *
 * Processes payments ensuring no overpayment, updates loan status,
 * and persists payment records.
 *
 * @author Khanyisani Luyanda Ntabeni
 */
@Service
@AllArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private LoanService loanService;
    private PaymentRepository paymentRepository;

    /**
     * Processes a payment towards a loan.
     * Validates the payment amount, updates remaining balance,
     * settles loan if fully paid, and persists payment record.
     *
     * @param request PaymentRequest containing loan ID and payment amount
     * @return Persisted Payment entity
     * @throws IllegalArgumentException if payment exceeds remaining balance
     * @throws IllegalStateException if loan is already settled
     */

    @Transactional
    public Payment processPayment(PaymentRequest request) {
        log.info("Processing payment for loanId={}, amount={}", request.getLoanId(), request.getPaymentAmount());

        Loan loan = loanService.getLoan(request.getLoanId());

        if (loan.getStatus() == LoanStatus.SETTLED) {
            log.warn("Attempt to pay on already settled loan with loanId={}", loan.getLoanId());
            throw new IllegalStateException("Loan is already settled.");
        }

        BigDecimal remaining = loan.getRemainingBalance();
        BigDecimal paymentAmt = request.getPaymentAmount();

        if (paymentAmt.compareTo(remaining) > 0) {
            log.warn("Payment amount {} exceeds remaining balance {} for loanId={}", paymentAmt, remaining, loan.getLoanId());
            throw new IllegalArgumentException("Payment exceeds remaining balance");
        }

        // Update loan remaining balance
        BigDecimal newBalance = remaining.subtract(paymentAmt);
        loan.setRemainingBalance(newBalance);
        log.info("Updated loanId={} remaining balance from {} to {}", loan.getLoanId(), remaining, newBalance);

        // Set loan status to SETTLED if fully paid
        if (newBalance.compareTo(BigDecimal.ZERO) == 0) {
            loan.setStatus(LoanStatus.SETTLED);
            log.info("Loan loanId={} fully paid and marked as SETTLED", loan.getLoanId());
        }

        loanService.updateLoan(loan);

        Payment payment = Payment.builder()
                .loanId(request.getLoanId())
                .paymentAmount(paymentAmt)
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment saved with paymentId={} for loanId={}", savedPayment.getPaymentId(), savedPayment.getLoanId());

        return savedPayment;
    }
}
