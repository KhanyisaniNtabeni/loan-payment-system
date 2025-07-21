package com.bancx.loanpayment.loan.integ;

import com.bancx.loanpayment.exception.GlobalExceptionHandler;
import com.bancx.loanpayment.loan.rest.LoanRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
class LoanControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createLoan_andGetLoan_success() throws Exception {
        LoanRequest request = LoanRequest.builder()
                .loanAmount(new BigDecimal("2500.00"))
                .term(18)
                .build();

        // Create loan
        String jsonResponse = mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.loanId").exists())
                .andExpect(jsonPath("$.loanAmount").value(2500.00))
                .andExpect(jsonPath("$.term").value(18))
                .andReturn().getResponse().getContentAsString();

        // Extract loanId
        Long loanId = objectMapper.readTree(jsonResponse).get("loanId").asLong();

        // Retrieve loan
        mockMvc.perform(get("/loans/" + loanId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanId").value(loanId))
                .andExpect(jsonPath("$.loanAmount").value(2500.00))
                .andExpect(jsonPath("$.term").value(18));
    }

    @Test
    void getLoan_notFound_returns404() throws Exception {
        mockMvc.perform(get("/loans/99999"))
                .andExpect(status().isNotFound());
    }
}
