package com.example.management.domain.model;

import com.example.management.domain.exception.InvalidOrderException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Entity representing an order.
 * Invariants: at least one line; totalAmount = sum of line totals.
 */
public final class Order {

    private final String id;
    private final String customerId;
    private final List<OrderLine> lines;
    private final BigDecimal totalAmount;

    private Order(String id, String customerId, List<OrderLine> lines, BigDecimal totalAmount) {
        this.id = id;
        this.customerId = Objects.requireNonNull(customerId, "customerId");
        this.lines = Collections.unmodifiableList(new ArrayList<>(lines));
        this.totalAmount = totalAmount;
    }

    /** Creates a new order (not yet persisted; id is null). */
    public static Order create(String customerId, List<OrderLine> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new InvalidOrderException("Order must have at least one line");
        }
        Objects.requireNonNull(customerId, "customerId");
        BigDecimal total = lines.stream()
            .map(OrderLine::getLineTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Order(null, customerId, lines, total);
    }

    /** Reconstructs an order from persistence (with id). */
    public static Order fromPersisted(String id, String customerId, List<OrderLine> lines, BigDecimal totalAmount) {
        Objects.requireNonNull(id, "id");
        if (lines == null || lines.isEmpty()) {
            throw new InvalidOrderException("Order must have at least one line");
        }
        return new Order(id, customerId, lines, totalAmount);
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<OrderLine> getLines() {
        return lines;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}
