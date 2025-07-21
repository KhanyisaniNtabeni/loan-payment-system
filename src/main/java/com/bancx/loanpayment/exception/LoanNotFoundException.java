package com.bancx.loanpayment.exception;

/**
 * Exception thrown when a requested loan entity
 * cannot be found in the system.
 *
 * Used to indicate HTTP 404 Not Found scenarios.
 *
 * Author: Khanyisani Luyanda Ntabeni
 */
public class LoanNotFoundException extends RuntimeException {

    /**
     * Constructs a new LoanNotFoundException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public LoanNotFoundException(String message) {
        super(message);
    }
}
