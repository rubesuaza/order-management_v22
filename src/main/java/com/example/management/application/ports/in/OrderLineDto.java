package com.example.management.application.ports.in;

import java.math.BigDecimal;

/**
 * DTO for a single order line in create-order requests.
 */
public record OrderLineDto(String productId, int quantity, BigDecimal unitPrice) {
}
