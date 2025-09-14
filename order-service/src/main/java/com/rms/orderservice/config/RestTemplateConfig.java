package com.rms.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.slf4j.MDC;

import java.util.Collections;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(traceIdInterceptor()));
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestInterceptor traceIdInterceptor() {
        return (request, body, execution) -> {
            String traceId = MDC.get("traceId");
            if (traceId != null) {
                request.getHeaders().add("trace-id", traceId);
            }
            return execution.execute(request, body);
        };
    }
}
