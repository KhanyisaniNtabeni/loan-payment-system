package com.bancx.loanpayment.loan.controller;

import com.bancx.loanpayment.loan.entity.Loan;
import com.bancx.loanpayment.loan.rest.LoanRequest;
import com.bancx.loanpayment.loan.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing loans.
 * Provides endpoints to create and retrieve loans.
 *
 * @author Khanyisani Luyanda Ntabeni
 */
@RestController
@RequestMapping("/loans")
@AllArgsConstructor
@Slf4j
public class LoanController {

    private LoanService loanService;

    /**
     * Endpoint to create a new loan.
     *
     * @param request LoanRequest containing loan amount and term
     * @return ResponseEntity with created Loan and HTTP 201 status
     */
    @Operation(summary = "Create a new loan")
    @PostMapping
    public ResponseEntity<Loan> createLoan(@Valid @RequestBody LoanRequest request) {
        log.info("Received request to create loan with amount: {} and term: {}",
                request.getLoanAmount(), request.getTerm());

        Loan createdLoan = loanService.createLoan(request);

        log.info("Created loan with ID: {}", createdLoan.getLoanId());
        return new ResponseEntity<>(createdLoan, HttpStatus.CREATED);
    }

    /**
     * Endpoint to retrieve a loan by its ID.
     *
     * @param loanId ID of the loan to retrieve
     * @return ResponseEntity with the Loan entity and HTTP 200 status
     */
    @GetMapping("/{loanId}")
    @Operation(summary = "Get loan by ID")
    public ResponseEntity<Loan> getLoan(@PathVariable Long loanId) {
        log.info("Received request to get loan with ID: {}", loanId);

        Loan loan = loanService.getLoan(loanId);

        log.info("Returning loan with ID: {}", loanId);
        return ResponseEntity.ok(loan);
    }
}
