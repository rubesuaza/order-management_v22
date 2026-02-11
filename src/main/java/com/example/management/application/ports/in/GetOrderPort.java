package com.example.management.application.ports.in;

import com.example.management.domain.model.Order;

import java.util.Optional;

/**
 * Input port for retrieving an order by id.
 */
public interface GetOrderPort {

    Optional<Order> getOrderById(String id);
}
