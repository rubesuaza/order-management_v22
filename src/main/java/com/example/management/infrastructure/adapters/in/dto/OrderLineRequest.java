package com.example.management.infrastructure.adapters.in.dto;

import java.math.BigDecimal;

/**
 * REST request item for an order line.
 */
public record OrderLineRequest(String productId, int quantity, BigDecimal unitPrice) {
}
