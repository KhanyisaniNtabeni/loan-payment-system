package com.bancx.loanpayment.payment.repository;

import com.bancx.loanpayment.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Payment entities.
 * Extends JpaRepository to provide standard CRUD operations
 * and JPA data access methods for Payment entities.
 *
 * Spring Data JPA will automatically generate the implementation.
 *
 * @author Khanyisani Luyanda Ntabeni
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {}
