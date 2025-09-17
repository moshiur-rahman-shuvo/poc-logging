package com.rms.orderservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationClient {
    private final RestTemplate restTemplate;

    @Value("${notification.url}")
    private String notificationUrl;

    @Autowired
    public NotificationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendNotification(NotificationRequest request, String traceId) {
        HttpHeaders headers = new HttpHeaders();
        if (traceId != null && !traceId.isEmpty()) {
            headers.set("traceid", traceId);
        }
        HttpEntity<NotificationRequest> entity = new HttpEntity<>(request, headers);
        restTemplate.postForEntity(notificationUrl, entity, Void.class);
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
