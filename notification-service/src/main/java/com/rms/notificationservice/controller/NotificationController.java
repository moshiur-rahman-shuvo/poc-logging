package com.rms.notificationservice.controller;
import com.rms.notificationservice.service.NotificationService;
import com.rms.notificationservice.dto.NotificationRequest;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @Autowired
    private Environment environment;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<Void> sendNotification(@RequestBody NotificationRequest request) {
        log.info("Sending Notification: {} ", request);
        notificationService.sendNotification(request.getUserId(), request.getMessage());
        return ResponseEntity.ok().build();
    }
}
