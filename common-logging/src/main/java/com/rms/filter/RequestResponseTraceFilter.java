package com.rms.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestResponseTraceFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestResponseTraceFilter.class);
    private static final String TRACE_HEADER = "trace-id";
    private static final String MDC_KEY = "traceId";

    @Override
    protected void doFilterInternal(HttpServletRequest request0,
                                    HttpServletResponse response0,
                                    FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper request = new ContentCachingRequestWrapper(request0);
        ContentCachingResponseWrapper response = new ContentCachingResponseWrapper(response0);

        // Get traceId from header or generate
        String traceIdHeader = request.getHeader(TRACE_HEADER);
        String traceId = Optional.ofNullable(traceIdHeader)
                .filter(h -> !h.isBlank())
                .orElse(UUID.randomUUID().toString().replace("-", ""));
        MDC.put(MDC_KEY, traceId);

        long start = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } finally {
            long durationMs = System.currentTimeMillis() - start;

            String requestBody = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);
            if (requestBody.isBlank()) requestBody = "{}";

            String responseBody = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
            if (responseBody.isBlank()) responseBody = "{}";

            // Log single line per service
            log.info("[Service] {} {} status={} rtt={}ms traceId={} request={} response={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    durationMs,
                    traceId,
                    requestBody.replaceAll("\\s+", " "),  // optional: single-line format
                    responseBody.replaceAll("\\s+", " ")
            );

            // Copy response back
            response.copyBodyToResponse();
            MDC.clear();
        }
    }
}
