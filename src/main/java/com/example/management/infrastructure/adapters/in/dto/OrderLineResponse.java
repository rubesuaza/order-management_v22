package com.example.management.infrastructure.adapters.in.dto;

import com.example.management.domain.model.OrderLine;

import java.math.BigDecimal;

/**
 * REST response item for an order line.
 */
public record OrderLineResponse(String productId, int quantity, BigDecimal unitPrice, BigDecimal lineTotal) {
    public static OrderLineResponse from(OrderLine line) {
        return new OrderLineResponse(
            line.getProductId(),
            line.getQuantity(),
            line.getUnitPrice(),
            line.getLineTotal()
        );
    }
}
