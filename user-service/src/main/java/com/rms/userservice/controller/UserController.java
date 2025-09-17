package com.rms.userservice.controller;

import com.rms.userservice.entity.Users;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.rms.userservice.service.UserService;
import com.rms.userservice.client.NotificationClient;
import com.rms.userservice.client.OrderClient;
import com.rms.filter.RequestResponseLogger;

@Slf4j
@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final NotificationClient notificationClient;
    private final OrderClient orderClient;
    @Autowired
    private Environment environment;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody Users users,
                                        @RequestHeader(value = "traceid", required = false) String traceId) {
        Users savedUser = userService.createUser(users);
        log.info("User created: {} sending notification email", savedUser);
        notificationClient.sendNotification(savedUser, "Welcome to our service!", traceId);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/order")
    public ResponseEntity<?> placeOrder(@RequestBody OrderClient.OrderRequest orderRequest,
                                        @RequestHeader(value = "traceid", required = false) String traceId,
                                        jakarta.servlet.http.HttpServletRequest request) {
        RequestResponseLogger.logRequest(request, environment, "UserController", orderRequest, traceId);
        OrderClient.OrderResponse orderResponse = orderClient.placeOrder(orderRequest, traceId);
        return ResponseEntity.ok(orderResponse);
    }
}
