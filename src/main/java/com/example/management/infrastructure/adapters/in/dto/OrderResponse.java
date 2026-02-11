package com.example.management.infrastructure.adapters.in.dto;

import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderLine;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST response for an order.
 */
public record OrderResponse(
    String id,
    String customerId,
    BigDecimal totalAmount,
    List<OrderLineResponse> lines
) {
    public static OrderResponse from(Order order) {
        List<OrderLineResponse> lineResponses = order.getLines().stream()
            .map(OrderLineResponse::from)
            .collect(Collectors.toList());
        return new OrderResponse(
            order.getId(),
            order.getCustomerId(),
            order.getTotalAmount(),
            lineResponses
        );
    }
}
