package com.example.management.application.services;

import com.example.management.application.ports.in.OrderLineDto;
import com.example.management.application.ports.out.OrderRepository;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderApplicationService")
class OrderApplicationServiceTest {

    @Mock
    private OrderRepository orderRepository;

    private OrderApplicationService service;

    @BeforeEach
    void setUp() {
        service = new OrderApplicationService(orderRepository);
    }

    @Nested
    @DisplayName("createOrder")
    class CreateOrder {

        @Test
        @DisplayName("maps DTOs to domain, creates order, saves and returns saved order")
        void createsOrderAndReturnsSaved() {
            List<OrderLineDto> dtos = List.of(
                new OrderLineDto("p1", 2, new BigDecimal("10.00")),
                new OrderLineDto("p2", 1, new BigDecimal("5.50"))
            );
            Order created = Order.create("customer-1",
                List.of(
                    OrderLine.of("p1", 2, new BigDecimal("10.00")),
                    OrderLine.of("p2", 1, new BigDecimal("5.50"))
                ));
            Order saved = Order.fromPersisted("order-123", "customer-1", created.getLines(), new BigDecimal("25.50"));
            when(orderRepository.save(any(Order.class))).thenReturn(saved);

            Order result = service.createOrder("customer-1", dtos);

            assertThat(result.getId()).isEqualTo("order-123");
            assertThat(result.getCustomerId()).isEqualTo("customer-1");
            assertThat(result.getTotalAmount()).isEqualByComparingTo("25.50");

            ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
            verify(orderRepository).save(orderCaptor.capture());
            Order passedOrder = orderCaptor.getValue();
            assertThat(passedOrder.getId()).isNull();
            assertThat(passedOrder.getTotalAmount()).isEqualByComparingTo("25.50");
            assertThat(passedOrder.getLines()).hasSize(2);
        }

        @Test
        @DisplayName("throws when lines are empty (domain invariant)")
        void throwsWhenLinesEmpty() {
            assertThatThrownBy(() -> service.createOrder("c1", List.of()))
                .hasMessageContaining("at least one line");
        }

        @Test
        @DisplayName("throws when line has invalid data (e.g. negative quantity)")
        void throwsWhenLineInvalid() {
            List<OrderLineDto> dtos = List.of(new OrderLineDto("p1", -1, BigDecimal.ONE));
            assertThatThrownBy(() -> service.createOrder("c1", dtos))
                .hasMessageContaining("Quantity");
        }
    }

    @Nested
    @DisplayName("getOrderById")
    class GetOrderById {

        @Test
        @DisplayName("returns order when repository has it")
        void returnsOrderWhenFound() {
            Order order = Order.fromPersisted("id-1", "cust-1",
                List.of(OrderLine.of("p1", 1, new BigDecimal("10.00"))),
                new BigDecimal("10.00"));
            when(orderRepository.findById("id-1")).thenReturn(Optional.of(order));

            Optional<Order> result = service.getOrderById("id-1");

            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo("id-1");
            verify(orderRepository).findById("id-1");
        }

        @Test
        @DisplayName("returns empty when repository has no order")
        void returnsEmptyWhenNotFound() {
            when(orderRepository.findById("unknown")).thenReturn(Optional.empty());

            Optional<Order> result = service.getOrderById("unknown");

            assertThat(result).isEmpty();
            verify(orderRepository).findById("unknown");
        }
    }
}
