package com.bancx.loanpayment.payment.validation;

import com.bancx.loanpayment.payment.rest.PaymentRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void initValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validPaymentRequest_passesValidation() {
        PaymentRequest request = PaymentRequest.builder()
                .loanId(1L)
                .paymentAmount(new BigDecimal("0.50"))
                .build();

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void nullLoanId_failsValidation() {
        PaymentRequest request = PaymentRequest.builder()
                .loanId(null)
                .paymentAmount(new BigDecimal("5.00"))
                .build();

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    void paymentAmountLessThanMin_failsValidation() {
        PaymentRequest request = PaymentRequest.builder()
                .loanId(1L)
                .paymentAmount(new BigDecimal("0.00"))
                .build();

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }
}
