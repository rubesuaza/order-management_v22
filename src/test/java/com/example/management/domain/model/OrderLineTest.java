package com.example.management.domain.model;

import com.example.management.domain.exception.InvalidOrderLineException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("OrderLine")
class OrderLineTest {

    @Nested
    @DisplayName("creation")
    class Creation {

        @Test
        @DisplayName("creates valid line and lineTotal is quantity * unitPrice")
        void createsValidLineWithCorrectTotal() {
            OrderLine line = OrderLine.of("product-1", 2, new BigDecimal("10.50"));
            assertEquals("product-1", line.getProductId());
            assertEquals(2, line.getQuantity());
            assertEquals(new BigDecimal("10.50"), line.getUnitPrice());
            assertEquals(new BigDecimal("21.00"), line.getLineTotal());
        }

        @Test
        @DisplayName("rejects quantity zero")
        void rejectsQuantityZero() {
            assertThrows(InvalidOrderLineException.class,
                () -> OrderLine.of("p1", 0, new BigDecimal("10")));
        }

        @Test
        @DisplayName("rejects negative quantity")
        void rejectsNegativeQuantity() {
            assertThrows(InvalidOrderLineException.class,
                () -> OrderLine.of("p1", -1, new BigDecimal("10")));
        }

        @Test
        @DisplayName("rejects negative unit price")
        void rejectsNegativeUnitPrice() {
            assertThrows(InvalidOrderLineException.class,
                () -> OrderLine.of("p1", 1, new BigDecimal("-0.01")));
        }

        @Test
        @DisplayName("accepts zero unit price")
        void acceptsZeroUnitPrice() {
            OrderLine line = OrderLine.of("p1", 3, BigDecimal.ZERO);
            assertEquals(BigDecimal.ZERO, line.getLineTotal());
        }
    }
}
