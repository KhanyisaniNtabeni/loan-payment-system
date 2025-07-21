package com.bancx.loanpayment.loan.entity;

import com.bancx.loanpayment.util.LoanStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Version;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



import java.math.BigDecimal;

/**
 * Entity representing a Loan record.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    /**
     * Total loan amount issued.
     */
    private BigDecimal loanAmount;

    /**
     * Loan term duration in months.
     */
    private Integer term;

    /**
     * Loan status - ACTIVE or SETTLED.
     */
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private LoanStatus status = LoanStatus.ACTIVE;

    /**
     * Remaining balance to be paid by borrower.
     */
    private BigDecimal remainingBalance;

    /**
     * Optimistic locking version column to prevent concurrent update conflicts.
     */
    @Version
    private Integer version;

    /**
     * Initializes remaining balance before saving.
     */
    @PrePersist
    public void prePersist() {
        this.remainingBalance = this.loanAmount;
    }
}
