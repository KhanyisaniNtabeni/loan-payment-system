package com.bancx.loanpayment.payment.rest;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO representing the request payload for making a payment towards a loan.
 * Contains loan ID and payment amount with validation constraints.
 *
 * Validates that loanId is not null and paymentAmount is greater than zero.
 *
 * Uses Lombok annotations for boilerplate code generation.
 *
 * @author Khanyisani Luyanda Ntabeni
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {

    /**
     * The ID of the loan to which the payment applies.
     * Cannot be null.
     */
    @NotNull(message = "Loan ID is required")
    private Long loanId;

    /**
     * The amount of the payment to make.
     * Must be at least 0.01.
     */
    @NotNull(message = "Payment amount is required")
    @DecimalMin(value = "0.01", message = "Payment amount must be greater than 0")
    private BigDecimal paymentAmount;

}
