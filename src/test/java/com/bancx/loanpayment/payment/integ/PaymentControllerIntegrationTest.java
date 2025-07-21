package com.bancx.loanpayment.payment.integ;

import com.bancx.loanpayment.exception.GlobalExceptionHandler;
import com.bancx.loanpayment.loan.entity.Loan;
import com.bancx.loanpayment.loan.repoaitory.LoanRepository;
import com.bancx.loanpayment.payment.rest.PaymentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
class PaymentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long loanId;

    @BeforeEach
    void setUp() {
        Loan loan = Loan.builder()
                .loanAmount(new BigDecimal("1000.00"))
                .remainingBalance(new BigDecimal("1000.00"))
                .term(12)
                .build();
        loan = loanRepository.save(loan);
        loanId = loan.getLoanId();
    }

    @Test
    void makePayment_validRequest_reducesBalanceAndReturnsCreated() throws Exception {
        PaymentRequest request = PaymentRequest.builder()
                .loanId(loanId)
                .paymentAmount(new BigDecimal("500.00"))
                .build();

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.loanId").value(loanId))
                .andExpect(jsonPath("$.paymentAmount").value(500.00));
    }

    @Test
    void makePayment_overpayment_returnsBadRequest() throws Exception {
        PaymentRequest request = PaymentRequest.builder()
                .loanId(loanId)
                .paymentAmount(new BigDecimal("1500.00"))
                .build();

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
