package com.example.management.infrastructure.adapters.in.dto;

import com.example.management.application.ports.in.OrderLineDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST request body for creating an order.
 */
public record CreateOrderRequest(String customerId, List<OrderLineRequest> lines) {

    public List<OrderLineDto> toOrderLineDtos() {
        if (lines == null) return List.of();
        return lines.stream()
            .map(l -> new OrderLineDto(l.productId(), l.quantity(), l.unitPrice()))
            .collect(Collectors.toList());
    }
}
