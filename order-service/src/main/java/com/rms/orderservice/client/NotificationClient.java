package com.rms.orderservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationClient {
    private final RestTemplate restTemplate;

    @Autowired
    public NotificationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendNotification(NotificationRequest request) {
        restTemplate.postForEntity("http://localhost:8083/notification/api/notifications", request, Void.class);
    }

    public static class NotificationRequest {
        private Long userId;
        private String message;
        public NotificationRequest() {}
        public NotificationRequest(Long userId, String message) {
            this.userId = userId;
            this.message = message;
        }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}

