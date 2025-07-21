package com.bancx.loanpayment.loan.repoaitory;

import com.bancx.loanpayment.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Loan entities.
 * Extends JpaRepository to provide CRUD operations and
 * JPA data access methods for Loan entities.
 *
 * Spring will automatically create an implementation
 * at runtime.
 *
 * @author Khanyisani Luyanda Ntabeni
 */
@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {}
