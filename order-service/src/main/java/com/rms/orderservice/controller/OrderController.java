package com.rms.orderservice.controller;

import com.rms.orderservice.entity.Order;
import com.rms.orderservice.service.OrderService;
import com.rms.orderservice.client.NotificationClient;
import com.rms.filter.RequestResponseLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final NotificationClient notificationClient;

    @Autowired
    private Environment environment;

    @Autowired
    public OrderController(OrderService orderService, NotificationClient notificationClient) {
        this.orderService = orderService;
        this.notificationClient = notificationClient;
    }

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody Order order,
                                            @RequestHeader(value = "traceid", required = false) String traceId,
                                            jakarta.servlet.http.HttpServletRequest request) {
        RequestResponseLogger.logRequest(request, environment, "OrderController", order, traceId);
        Order savedOrder = null;
        try {
            savedOrder = orderService.placeOrder(order);
        } catch (Exception e) {
            RequestResponseLogger.logRequest(request, environment, "OrderController", "Error placing order: " + e.getMessage(), traceId);
            return ResponseEntity.badRequest().build();
        }
        String message = "Your order for product '" + savedOrder.getProduct() + "' has been placed successfully.";
        RequestResponseLogger.logRequest(request, environment, "NotificationClient", message, traceId);
        notificationClient.sendNotification(new NotificationClient.NotificationRequest(savedOrder.getUserId(), message), traceId);
        return ResponseEntity.ok(savedOrder);
    }
}
