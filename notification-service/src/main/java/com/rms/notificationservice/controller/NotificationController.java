package com.rms.notificationservice.controller;

import com.rms.notificationservice.service.NotificationService;
import com.rms.notificationservice.dto.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<Void> sendNotification(@RequestBody NotificationRequest request) {
        log.info("Sending notification to user: {}", request.getUser());
        notificationService.sendNotification(request.getUser(), request.getMessage());
        return ResponseEntity.ok().build();
    }
}

