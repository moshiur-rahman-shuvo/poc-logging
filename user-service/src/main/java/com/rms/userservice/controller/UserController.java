package com.rms.userservice.controller;

import com.rms.userservice.entity.Users;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.rms.userservice.service.UserService;
import com.rms.userservice.client.NotificationClient;
import com.rms.userservice.client.OrderClient;

@Slf4j
@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final NotificationClient notificationClient;
    private final OrderClient orderClient;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody Users users, @RequestHeader(value = "traceid", required = false) String traceId) {
        Users savedUser = userService.createUser(users);
        log.info("User created: {} sending notification email", savedUser);
        notificationClient.sendNotification(savedUser, "Welcome to our service!", traceId);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/order")
    public ResponseEntity<?> placeOrder(@RequestBody OrderClient.OrderRequest orderRequest, @RequestHeader(value = "traceid", required = false) String traceId) {
        log.info("Received order request: {} {} {} {}", orderRequest.getUserId(), orderRequest.getProduct(),orderRequest.getQuantity(),orderRequest.getPrice());
        OrderClient.OrderResponse orderResponse = orderClient.placeOrder(orderRequest, traceId);
        // Send notification after successful order placement
        Users user = new Users();
        user.setId(orderRequest.getUserId());
        String message = "Your order for product '" + orderRequest.getProduct() + "' has been placed successfully.";
        notificationClient.sendNotification(user, message, traceId);
        return ResponseEntity.ok(orderResponse);
    }
}
