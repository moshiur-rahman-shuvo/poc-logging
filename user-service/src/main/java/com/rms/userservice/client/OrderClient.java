package com.rms.userservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

@Service
public class OrderClient {
    private final RestTemplate restTemplate;

    @Value("${order.url}")
    private String orderUrl;

    @Autowired
    public OrderClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public OrderResponse placeOrder(OrderRequest orderRequest, String traceId) {
        HttpHeaders headers = new HttpHeaders();
        if (traceId != null && !traceId.isEmpty()) {
            headers.set("traceid", traceId);
        }
        HttpEntity<OrderRequest> entity = new HttpEntity<>(orderRequest, headers);
        ResponseEntity<OrderResponse> response = restTemplate.postForEntity(
            orderUrl, entity, OrderResponse.class);
        return response.getBody();
    }

    public static class OrderRequest {
        private Long userId;
        private String product;
        private Integer quantity;
        private Double price;
        public OrderRequest() {}
        public OrderRequest(Long userId, String product, Integer quantity, Double price) {
            this.userId = userId;
            this.product = product;
            this.quantity = quantity;
            this.price = price;
        }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getProduct() { return product; }
        public void setProduct(String product) { this.product = product; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
    }

    public static class OrderResponse {
        private Long id;
        private Long userId;
        private String product;
        private Integer quantity;
        private Double price;
        public OrderResponse() {}
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getProduct() { return product; }
        public void setProduct(String product) { this.product = product; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
    }
}
