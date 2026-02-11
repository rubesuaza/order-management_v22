package com.example.management.infrastructure.adapters.out;

import com.example.management.application.ports.out.OrderRepository;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Contract tests for any OrderRepository implementation.
 * InMemoryOrderRepository is the implementation under test.
 */
@DisplayName("OrderRepository contract")
class InMemoryOrderRepositoryContractTest {

    private OrderRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryOrderRepository();
    }

    @Nested
    @DisplayName("save and findById")
    class SaveAndFind {

        @Test
        @DisplayName("saves order and assigns id")
        void savesOrderAndAssignsId() {
            OrderLine line = OrderLine.of("p1", 2, new BigDecimal("10.00"));
            Order created = Order.create("customer-1", List.of(line));
            assertEquals(null, created.getId());

            Order saved = repository.save(created);
            assertTrue(saved.getId() != null && !saved.getId().isEmpty());
            assertEquals("customer-1", saved.getCustomerId());
            assertEquals(new BigDecimal("20.00"), saved.getTotalAmount());
        }

        @Test
        @DisplayName("findById returns saved order")
        void findByIdReturnsSavedOrder() {
            OrderLine line = OrderLine.of("p1", 1, new BigDecimal("5.50"));
            Order saved = repository.save(Order.create("cust-1", List.of(line)));
            String id = saved.getId();

            Optional<Order> found = repository.findById(id);
            assertTrue(found.isPresent());
            assertEquals(id, found.get().getId());
            assertEquals("cust-1", found.get().getCustomerId());
            assertEquals(new BigDecimal("5.50"), found.get().getTotalAmount());
        }

        @Test
        @DisplayName("findById returns empty for unknown id")
        void findByIdReturnsEmptyForUnknownId() {
            Optional<Order> found = repository.findById("non-existent");
            assertTrue(found.isEmpty());
        }
    }
}
