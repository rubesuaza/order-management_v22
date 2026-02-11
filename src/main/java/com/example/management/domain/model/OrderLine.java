package com.example.management.domain.model;

import com.example.management.domain.exception.InvalidOrderLineException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value object representing a single line in an order.
 * Invariants: quantity > 0, unitPrice >= 0, lineTotal = quantity * unitPrice.
 */
public final class OrderLine {

    private static final int SCALE = 2;

    private final String productId;
    private final int quantity;
    private final BigDecimal unitPrice;
    private final BigDecimal lineTotal;

    private OrderLine(String productId, int quantity, BigDecimal unitPrice) {
        this.productId = Objects.requireNonNull(productId, "productId");
        this.quantity = quantity;
        this.unitPrice = unitPrice.setScale(SCALE, RoundingMode.HALF_UP);
        this.lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity)).setScale(SCALE, RoundingMode.HALF_UP);
    }

    public static OrderLine of(String productId, int quantity, BigDecimal unitPrice) {
        if (quantity <= 0) {
            throw new InvalidOrderLineException("Quantity must be positive, got: " + quantity);
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidOrderLineException("Unit price must be non-negative, got: " + unitPrice);
        }
        return new OrderLine(productId, quantity, unitPrice);
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLine orderLine = (OrderLine) o;
        return quantity == orderLine.quantity
            && Objects.equals(productId, orderLine.productId)
            && Objects.equals(unitPrice, orderLine.unitPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, quantity, unitPrice);
    }
}
