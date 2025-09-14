package com.rms.notificationservice.controller;

import com.rms.notificationservice.service.NotificationService;
import com.rms.notificationservice.dto.NotificationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<Void> sendNotification(@RequestBody NotificationRequest request) {
        notificationService.sendNotification(request.getUser(), request.getMessage());
        return ResponseEntity.ok().build();
    }
}

