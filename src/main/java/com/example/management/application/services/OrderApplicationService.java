package com.example.management.application.services;

import com.example.management.application.ports.in.CreateOrderPort;
import com.example.management.application.ports.in.GetOrderPort;
import com.example.management.application.ports.in.OrderLineDto;
import com.example.management.application.ports.out.OrderRepository;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderLine;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Application service implementing input ports for order use cases.
 * Depends only on output ports (OrderRepository); no infrastructure.
 */
@Service
public class OrderApplicationService implements CreateOrderPort, GetOrderPort {

    private final OrderRepository orderRepository;

    public OrderApplicationService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(String customerId, List<OrderLineDto> lines) {
        List<OrderLine> domainLines = lines.stream()
            .map(dto -> OrderLine.of(dto.productId(), dto.quantity(), dto.unitPrice()))
            .collect(Collectors.toList());
        Order order = Order.create(customerId, domainLines);
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }
}
