package com.rms.notificationservice.service;

import com.rms.notificationservice.dto.NotificationUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {
    public void sendNotification(NotificationUserDto user, String message) {
        log.info("Notification '{}' sent to user {}", message, user);
    }
}
