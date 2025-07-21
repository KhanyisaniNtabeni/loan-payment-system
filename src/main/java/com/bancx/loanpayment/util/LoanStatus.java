package com.bancx.loanpayment.util;

import lombok.Getter;

/**
 * Enum representing the possible statuses of a Loan.
 *
 * ACTIVE: Loan is ongoing and payments are expected.
 * SETTLED: Loan has been fully paid and closed.
 *
 * @author Khanyisani Luyanda Ntabeni
 */
@Getter
public enum LoanStatus {
    ACTIVE,
    SETTLED
}
