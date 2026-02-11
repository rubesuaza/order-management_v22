package com.example.management.domain.model;

import com.example.management.domain.exception.InvalidOrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Order")
class OrderTest {

    @Nested
    @DisplayName("creation")
    class Creation {

        @Test
        @DisplayName("creates order with lines and total is sum of line totals")
        void createsOrderWithCorrectTotal() {
            OrderLine l1 = OrderLine.of("p1", 2, new BigDecimal("10.00"));
            OrderLine l2 = OrderLine.of("p2", 1, new BigDecimal("5.50"));
            Order order = Order.create("customer-1", List.of(l1, l2));

            assertEquals("customer-1", order.getCustomerId());
            assertEquals(2, order.getLines().size());
            assertEquals(new BigDecimal("25.50"), order.getTotalAmount());
        }

        @Test
        @DisplayName("rejects empty lines")
        void rejectsEmptyLines() {
            assertThrows(InvalidOrderException.class,
                () -> Order.create("c1", List.of()));
        }

        @Test
        @DisplayName("rejects null lines")
        void rejectsNullLines() {
            assertThrows(InvalidOrderException.class,
                () -> Order.create("c1", null));
        }

        @Test
        @DisplayName("order total equals sum of line totals")
        void orderTotalEqualsSumOfLineTotals() {
            OrderLine l1 = OrderLine.of("a", 3, new BigDecimal("2.50"));
            OrderLine l2 = OrderLine.of("b", 1, new BigDecimal("7.00"));
            Order order = Order.create("c1", List.of(l1, l2));
            assertEquals(new BigDecimal("14.50"), order.getTotalAmount());
        }

        @Test
        @DisplayName("lines are immutable copy")
        void linesAreImmutableCopy() {
            OrderLine line = OrderLine.of("p1", 1, BigDecimal.ONE);
            Order order = Order.create("c1", List.of(line));
            List<OrderLine> returned = order.getLines();
            assertThrows(UnsupportedOperationException.class, () -> returned.add(OrderLine.of("p2", 1, BigDecimal.ONE)));
        }

        @Test
        @DisplayName("create leaves id null")
        void createLeavesIdNull() {
            Order order = Order.create("c1", List.of(OrderLine.of("p1", 1, BigDecimal.ONE)));
            assertEquals(null, order.getId());
        }
    }

    @Nested
    @DisplayName("fromPersisted")
    class FromPersisted {

        @Test
        @DisplayName("reconstructs order with id")
        void reconstructsOrderWithId() {
            OrderLine line = OrderLine.of("p1", 2, new BigDecimal("10.00"));
            Order order = Order.fromPersisted("order-123", "cust-1", List.of(line), new BigDecimal("20.00"));
            assertEquals("order-123", order.getId());
            assertEquals("cust-1", order.getCustomerId());
            assertEquals(new BigDecimal("20.00"), order.getTotalAmount());
        }
    }
}
