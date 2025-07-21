package com.bancx.loanpayment.payment.controller;

import com.bancx.loanpayment.payment.entity.Payment;
import com.bancx.loanpayment.payment.rest.PaymentRequest;
import com.bancx.loanpayment.payment.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PaymentControllerTest {

    private MockMvc mockMvc;
    private PaymentService paymentService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        paymentService = mock(PaymentService.class);
        PaymentController controller = new PaymentController(paymentService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void makePayment_validRequest_returnsCreated() throws Exception {
        PaymentRequest request = PaymentRequest.builder()
                .loanId(1L)
                .paymentAmount(new BigDecimal("150.00"))
                .build();

        Payment response = Payment.builder()
                .loanId(1L)
                .paymentAmount(request.getPaymentAmount())
                .paymentId(1L)
                .timestamp(LocalDateTime.now())
                .build();

        when(paymentService.processPayment(any(PaymentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.loanId").value(1L))
                .andExpect(jsonPath("$.paymentAmount").value(150.00));
    }

    @Test
    void makePayment_invalidRequest_returnsBadRequest() throws Exception {
        PaymentRequest request = PaymentRequest.builder()
                .loanId(null)
                .paymentAmount(null)
                .build();

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
