package com.bancx.loanpayment.loan.service;

import com.bancx.exception.LoanNotFoundException;
import com.bancx.loanpayment.loan.entity.Loan;
import com.bancx.loanpayment.loan.rest.LoanRequest;

/**
 * Service for managing Loan entities including creation,
 * retrieval, and updates such as balance adjustment and status change.
 */
public interface LoanService {

    /**
     * Creates a new loan in the database.
     *
     * @param loan The loan entity to be saved.
     * @return The saved Loan entity with ID.
     */
    public Loan createLoan(LoanRequest loan);

    /**
     * Retrieves a loan by its ID.
     *
     * @param loanId The ID of the loan.
     * @return The Loan entity.
     * @throws LoanNotFoundException if the loan doesn't exist.
     */
    public Loan getLoan(Long loanId);

    /**
     * Updates an existing loan, e.g., when balance or status changes.
     *
     * @param loan The loan to update.
     */
    public void updateLoan(Loan loan);
}
