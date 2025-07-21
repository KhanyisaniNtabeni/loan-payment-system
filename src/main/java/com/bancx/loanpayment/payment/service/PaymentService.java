package com.bancx.loanpayment.payment.service;


import com.bancx.loanpayment.payment.entity.Payment;

import com.bancx.loanpayment.payment.rest.PaymentRequest;
import org.springframework.stereotype.Service;



/**
 * Service that handles business logic for loan payments,
 * including validation, balance update, and loan settlement.
 */
@Service
public interface PaymentService {

    /**
     * Processes a payment towards a loan.
     * Ensures overpayment is prevented and settles the loan if balance reaches zero.
     *
     * @param payment Payment object with loan ID and amount.
     * @return Persisted Payment entity.
     * @throws IllegalArgumentException if payment exceeds remaining balance.
     */
    public Payment processPayment(PaymentRequest payment);
}
