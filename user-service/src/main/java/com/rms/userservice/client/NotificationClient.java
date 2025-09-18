package com.rms.userservice.client;

import com.rms.notificationservice.dto.NotificationRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NotificationClient {
    private final RestTemplate restTemplate;
    private static final Logger LOG = LoggerFactory.getLogger(NotificationClient.class);

    @Value("${notification.url}")
    private String notificationUrl;

    @Autowired
    public NotificationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendNotification(Long userID, String message, String traceId) {
        NotificationRequest request = new NotificationRequest(userID, message);
        HttpHeaders headers = new HttpHeaders();
        if (traceId != null && !traceId.isEmpty()) {
            headers.set("traceid", traceId);
        }
        LOG.info("Sending notification with headers: {}", headers);
        HttpEntity<NotificationRequest> entity = new HttpEntity<>(request, headers);
        restTemplate.postForEntity(notificationUrl, entity, Void.class);
    }

//    public static class NotificationUserDto {
//        private Long id;
//        private String name;
//        private String email;
//        private String address;
//        public NotificationUserDto() {}
//        public NotificationUserDto(Long id, String name, String email, String address) {
//            this.id = id;
//            this.name = name;
//            this.email = email;
//            this.address = address;
//        }
//        public Long getId() { return id; }
//        public void setId(Long id) { this.id = id; }
//        public String getName() { return name; }
//        public void setName(String name) { this.name = name; }
//        public String getEmail() { return email; }
//        public void setEmail(String email) { this.email = email; }
//        public String getAddress() { return address; }
//        public void setAddress(String address) { this.address = address; }
//    }

//    public static class NotificationRequest {
//        private Long userId ;
//        private String message;
//        public NotificationRequest() {}
//        public NotificationRequest(Long userId, String message) {
//            this.user = user;
//            this.message = message;
//        }
//        public NotificationUserDto getUser() { return user; }
//        public void setUser(NotificationUserDto user) { this.user = user; }
//        public String getMessage() { return message; }
//        public void setMessage(String message) { this.message = message; }
//    }
}
