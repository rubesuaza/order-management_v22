package com.example.management.infrastructure.adapters.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Contract tests for Order REST API (input adapter).
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("OrderController contract")
class OrderControllerContractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /orders creates order and returns 201 with body")
    void postOrdersCreatesOrderAndReturns201() throws Exception {
        Map<String, Object> body = Map.of(
            "customerId", "customer-1",
            "lines", List.of(
                Map.of("productId", "p1", "quantity", 2, "unitPrice", 10.00),
                Map.of("productId", "p2", "quantity", 1, "unitPrice", 5.50)
            )
        );

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.customerId").value("customer-1"))
            .andExpect(jsonPath("$.totalAmount").value(25.5))
            .andExpect(jsonPath("$.lines", hasSize(2)));
    }

    @Test
    @DisplayName("GET /orders/{id} returns 200 and order when exists")
    void getOrderByIdReturns200WhenExists() throws Exception {
        Map<String, Object> createBody = Map.of(
            "customerId", "cust-get",
            "lines", List.of(Map.of("productId", "p1", "quantity", 1, "unitPrice", 99.99))
        );
        String createResponse = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBody)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        String id = objectMapper.readTree(createResponse).get("id").asText();

        mockMvc.perform(get("/orders/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.customerId").value("cust-get"))
            .andExpect(jsonPath("$.totalAmount").value(99.99));
    }

    @Test
    @DisplayName("GET /orders/{id} returns 404 when not found")
    void getOrderByIdReturns404WhenNotFound() throws Exception {
        mockMvc.perform(get("/orders/non-existent-id"))
            .andExpect(status().isNotFound());
    }
}
