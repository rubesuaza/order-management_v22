package com.example.management.infrastructure.adapters.out;

import com.example.management.application.ports.out.OrderRepository;
import com.example.management.domain.model.Order;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of OrderRepository.
 * Assigns a UUID on save when order has no id. Suitable for tests and simple deployments.
 */
@Repository
public class InMemoryOrderRepository implements OrderRepository {

    private final Map<String, Order> store = new ConcurrentHashMap<>();

    @Override
    public Order save(Order order) {
        String id = order.getId() != null ? order.getId() : UUID.randomUUID().toString();
        Order toStore = order.getId() != null ? order : Order.fromPersisted(
            id, order.getCustomerId(), order.getLines(), order.getTotalAmount());
        store.put(id, toStore);
        return toStore;
    }

    @Override
    public Optional<Order> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }
}
