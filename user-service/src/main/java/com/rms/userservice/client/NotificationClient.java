package com.rms.userservice.client;

import com.rms.userservice.entity.Users;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class NotificationClient {
    private final RestTemplate restTemplate;

    @Autowired
    public NotificationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendNotification(Users user, String message) {
        NotificationUserDto userDto = new NotificationUserDto(user.getId(), user.getName(), user.getEmail(), user.getAddress());
        NotificationRequest request = new NotificationRequest(userDto, message);
        restTemplate.postForEntity("http://localhost:8083/notification/api/notifications", request, Void.class);
    }

    public static class NotificationUserDto {
        private Long id;
        private String name;
        private String email;
        private String address;
        public NotificationUserDto() {}
        public NotificationUserDto(Long id, String name, String email, String address) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.address = address;
        }
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
    }

    public static class NotificationRequest {
        private NotificationUserDto user;
        private String message;
        public NotificationRequest() {}
        public NotificationRequest(NotificationUserDto user, String message) {
            this.user = user;
            this.message = message;
        }
        public NotificationUserDto getUser() { return user; }
        public void setUser(NotificationUserDto user) { this.user = user; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
