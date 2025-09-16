package com.rms.orderservice.controller;

import com.rms.orderservice.entity.Order;
import com.rms.orderservice.service.OrderService;
import com.rms.orderservice.client.NotificationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final NotificationClient notificationClient;

    @Autowired
    public OrderController(OrderService orderService, NotificationClient notificationClient) {
        this.orderService = orderService;
        this.notificationClient = notificationClient;
    }

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
        Order savedOrder = null;
        try {
            savedOrder = orderService.placeOrder(order);
        } catch (Exception e) {
            log.error("Error placing order: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
        String message = "Your order for product '" + savedOrder.getProduct() + "' has been placed successfully.";
        notificationClient.sendNotification(new NotificationClient.NotificationRequest(savedOrder.getUserId(), message));
        return ResponseEntity.ok(savedOrder);
    }
}

