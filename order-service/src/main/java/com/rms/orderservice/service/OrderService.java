package com.rms.orderservice.service;

import com.rms.orderservice.entity.Order;
import com.rms.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order placeOrder(Order order) {
        if(order.getQuantity() <= 0) {
            throw new IllegalArgumentException("Invalid order quantity");
        }
        return orderRepository.save(order);
    }
}

