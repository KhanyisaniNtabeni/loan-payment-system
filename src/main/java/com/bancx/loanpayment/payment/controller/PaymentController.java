package com.bancx.loanpayment.payment.controller;

import com.bancx.loanpayment.payment.entity.Payment;
import com.bancx.loanpayment.payment.rest.PaymentRequest;
import com.bancx.loanpayment.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling payment operations.
 * Provides endpoint to make a payment towards a loan.
 *
 * Handles validation of incoming requests.
 *
 * @author Khanyisani Luyanda Ntabeni
 */
@RestController
@RequestMapping("/payments")
@AllArgsConstructor
@Slf4j
public class PaymentController {

    private PaymentService paymentService;

    /**
     * Endpoint to make a payment towards a loan.
     * Logs request details and payment creation.
     *
     * @param request the payment details including loan ID and amount
     * @return ResponseEntity containing the created Payment and HTTP status 201
     */
    @PostMapping
    @Operation(summary = "Create a new payment")
    public ResponseEntity<Payment> makePayment(@Valid @RequestBody PaymentRequest request) {
        log.info("Received payment request: loanId={}, amount={}", request.getLoanId(), request.getPaymentAmount());

        Payment payment = paymentService.processPayment(request);

        log.info("Payment created with paymentId={} for loanId={}", payment.getPaymentId(), payment.getLoanId());
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }
}
