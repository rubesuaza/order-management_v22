package com.example.management.application.ports.in;

import com.example.management.domain.model.Order;

import java.util.List;

/**
 * Input port for creating orders.
 * Used by input adapters (e.g. REST controller).
 */
public interface CreateOrderPort {

    /**
     * Creates and persists an order; returns the saved order with id.
     */
    Order createOrder(String customerId, List<OrderLineDto> lines);
}
