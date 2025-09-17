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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestResponseTraceFilter extends OncePerRequestFilter {

    // Named logger so we can route it separately in logback
    private static final Logger REQ_LOG = LoggerFactory.getLogger("REQUEST_LOGGER");
    private static final String TRACE_HEADER = "traceid";
    private static final String MDC_KEY = "traceId";
    private static final int MAX_PAYLOAD_LOG_LENGTH = 4096;
    private static final String ENV = "prod";

    private final Environment environment;

    public RequestResponseTraceFilter(Environment environment) {
        this.environment = environment;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request0,
                                    HttpServletResponse response0,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Wrap so we can read bodies after chain.doFilter
        ContentCachingRequestWrapper request = new ContentCachingRequestWrapper(request0);
        ContentCachingResponseWrapper response = new ContentCachingResponseWrapper(response0);

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

            String requestBody = readPayload(request.getContentAsByteArray(), request.getCharacterEncoding());
            String responseBody = readPayload(response.getContentAsByteArray(), response.getCharacterEncoding());
            String remoteAddr = Optional.ofNullable(request.getHeader("X-Forwarded-For"))
                    .orElseGet(request::getRemoteAddr);
            String requestTime = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z").format(new Date());
            String logLevel = (response.getStatus() >= 400) ? "ERROR" : "INFO";
            String className = request.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingHandler") != null
                ? request.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingHandler").getClass().getSimpleName()
                : request.getServletPath();
            String api = request.getRequestURI() + Optional.ofNullable(request.getQueryString()).map(q -> "?" + q).orElse("");
            String serviceName = getAppName();

            String combinedLog = RequestResponseLogFormatter.formatCombined(
                serviceName,
                className,
                request.getMethod(),
                api,
                logLevel,
                traceId,
                durationMs,
                requestTime,
                truncate(requestBody, MAX_PAYLOAD_LOG_LENGTH),
                truncate(responseBody, MAX_PAYLOAD_LOG_LENGTH)
            );

            if (response.getStatus() >= 400) {
                REQ_LOG.error(combinedLog);
            } else {
                REQ_LOG.info(combinedLog);
            }
            response.copyBodyToResponse();

            MDC.remove(MDC_KEY);
        }
    }

    private static String readPayload(byte[] buf, String charset) {
        if (buf == null || buf.length == 0) return "";
        try {
            return charset == null
                ? new String(buf, StandardCharsets.UTF_8)
                : new String(buf, charset);
        } catch (Exception ex) {
            return "[unknown]";
        }
    }

    private static String truncate(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        return s.substring(0, max) + "...(truncated)";
    }

    private String getAppName() {
        String appName = environment.getProperty("spring.application.name");
        if (appName == null || appName.isBlank()) {
            appName = "unknown-service";
        }
        return appName;
    }
}
