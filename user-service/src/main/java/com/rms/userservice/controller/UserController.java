package com.rms.userservice.controller;

import com.rms.userservice.entity.Users;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.rms.userservice.service.UserService;
import com.rms.userservice.client.NotificationClient;

@Slf4j
@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final NotificationClient notificationClient;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody Users users) {
        Users savedUser = userService.createUser(users);
        log.info("User created: {} sending notification email", savedUser);
        notificationClient.sendNotification(savedUser, "Welcome to our service!");
        return ResponseEntity.ok(savedUser);
    }
}
