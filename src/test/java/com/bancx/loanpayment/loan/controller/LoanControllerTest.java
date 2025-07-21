package com.bancx.loanpayment.loan.controller;

import com.bancx.loanpayment.exception.GlobalExceptionHandler;
import com.bancx.loanpayment.exception.LoanNotFoundException;
import com.bancx.loanpayment.loan.entity.Loan;
import com.bancx.loanpayment.loan.rest.LoanRequest;
import com.bancx.loanpayment.loan.service.LoanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class LoanControllerTest {

    private MockMvc mockMvc;
    private LoanService loanService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        loanService = Mockito.mock(LoanService.class);
        LoanController controller = new LoanController(loanService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createLoan_validRequest_returnsCreated() throws Exception {
        LoanRequest request = LoanRequest.builder()
                .loanAmount(new BigDecimal("1000.00"))
                .term(12)
                .build();

        Loan savedLoan = Loan.builder()
                .loanId(1L)
                .loanAmount(request.getLoanAmount())
                .term(request.getTerm())
                .build();

        when(loanService.createLoan(any(LoanRequest.class))).thenReturn(savedLoan);

        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.loanId").value(1L))
                .andExpect(jsonPath("$.loanAmount").value(1000.00))
                .andExpect(jsonPath("$.term").value(12));
    }

    @Test
    void createLoan_invalidRequest_returnsBadRequest() throws Exception {
        LoanRequest invalidRequest = LoanRequest.builder()
                .loanAmount(null) // invalid
                .term(0)          // invalid
                .build();

        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getLoan_existingLoan_returnsOk() throws Exception {
        Loan loan = Loan.builder()
                .loanId(1L)
                .loanAmount(new BigDecimal("500.00"))
                .term(6)
                .build();

        when(loanService.getLoan(1L)).thenReturn(loan);

        mockMvc.perform(get("/loans/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanId").value(1L))
                .andExpect(jsonPath("$.loanAmount").value(500.00))
                .andExpect(jsonPath("$.term").value(6));
    }

    @Test
    void getLoan_notFound_returnsNotFound() throws Exception {
        when(loanService.getLoan(999L)).thenThrow(new LoanNotFoundException("Loan ID 999 not found"));

        mockMvc.perform(get("/loans/999"))
                .andExpect(status().isNotFound());
    }
}
