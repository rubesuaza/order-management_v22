package com.example.management.application.ports.out;

import com.example.management.domain.model.Order;

import java.util.Optional;

/**
 * Output port for order persistence.
 * Implemented by infrastructure adapters (e.g. in-memory, JPA).
 */
public interface OrderRepository {

    /**
     * Saves the order and returns the persisted order (with id assigned).
     */
    Order save(Order order);

    Optional<Order> findById(String id);
}
