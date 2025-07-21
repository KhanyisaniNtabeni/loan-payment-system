package com.bancx.loanpayment.loan.rest;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

/**
 * DTO representing the request payload for creating or updating a loan.
 * Contains the loan amount and loan term, with validation constraints.
 *
 * @author Khanyisani Luyanda Ntabeni
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanRequest {

    /**
     * The amount of the loan to be issued.
     * Must not be null and must be greater than zero.
     */
    @NotNull(message = "Loan amount is required")
    @DecimalMin(value = "0.01", message = "Loan amount must be greater than 0")
    private BigDecimal loanAmount;

    /**
     * The duration of the loan term in months.
     * Must not be null and must be at least 1 month.
     */
    @NotNull(message = "Term is required")
    @Min(value = 1, message = "Term must be at least 1 month")
    private Integer term;

}
