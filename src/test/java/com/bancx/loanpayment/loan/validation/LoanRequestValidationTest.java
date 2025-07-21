package com.bancx.loanpayment.loan.validation;

import com.bancx.loanpayment.loan.rest.LoanRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LoanRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        validator = null;
    }

    @Test
    void validLoanRequest_noViolations() {
        LoanRequest request = LoanRequest.builder()
                .loanAmount(new BigDecimal("100.00"))
                .term(1)
                .build();

        Set<ConstraintViolation<LoanRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void loanAmountNull_shouldFailValidation() {
        LoanRequest request = LoanRequest.builder()
                .loanAmount(null)
                .term(1)
                .build();

        Set<ConstraintViolation<LoanRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("loanAmount")));
    }

    @Test
    void loanAmountLessThanMin_shouldFailValidation() {
        LoanRequest request = LoanRequest.builder()
                .loanAmount(new BigDecimal("0.00"))
                .term(1)
                .build();

        Set<ConstraintViolation<LoanRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("loanAmount")));
    }

    @Test
    void termNull_shouldFailValidation() {
        LoanRequest request = LoanRequest.builder()
                .loanAmount(new BigDecimal("100.00"))
                .term(null)
                .build();

        Set<ConstraintViolation<LoanRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("term")));
    }

    @Test
    void termLessThanMin_shouldFailValidation() {
        LoanRequest request = LoanRequest.builder()
                .loanAmount(new BigDecimal("100.00"))
                .term(0)
                .build();

        Set<ConstraintViolation<LoanRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("term")));
    }
}
