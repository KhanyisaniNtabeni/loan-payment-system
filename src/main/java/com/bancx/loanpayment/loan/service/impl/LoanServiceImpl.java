package com.bancx.loanpayment.loan.service.impl;

import com.bancx.loanpayment.exception.LoanNotFoundException;
import com.bancx.loanpayment.loan.entity.Loan;
import com.bancx.loanpayment.loan.repoaitory.LoanRepository;
import com.bancx.loanpayment.loan.rest.LoanRequest;
import com.bancx.loanpayment.loan.service.LoanService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

/**
 * Service for managing Loan entities including creation,
 * retrieval, and updates such as balance adjustment and status change.
 *
 * @author Khanyisani Luyanda Ntabeni
 */
@Service
@AllArgsConstructor
public class LoanServiceImpl implements LoanService {

    private LoanRepository loanRepository;

    /**
     * Creates a new loan in the database.
     *
     * @param request The loan entity to be saved.
     * @return The saved Loan entity with ID.
     */
    @Transactional
    public Loan createLoan(LoanRequest request) {
        return loanRepository.save(Loan.builder().loanAmount(request.getLoanAmount()).
                term(request.getTerm()).build());
    }

    /**
     * Retrieves a loan by its ID.
     *
     * @param loanId The ID of the loan.
     * @return The Loan entity.
     * @throws LoanNotFoundException if the loan doesn't exist.
     */

    public Loan getLoan(Long loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan ID " + loanId + " not found"));
    }

    /**
     * Updates an existing loan, e.g., when balance or status changes.
     *
     * @param loan The loan to update.
     */

    @Transactional
    public void updateLoan(Loan loan) {
        loanRepository.save(loan);
    }
}
