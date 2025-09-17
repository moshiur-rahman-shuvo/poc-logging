package com.rms.filter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestResponseLogger {
    private static final Logger REQ_LOG = LoggerFactory.getLogger("REQUEST_LOGGER");

    public static void logRequest(HttpServletRequest request, Environment env, String handler, Object payload, String traceId) {
        RequestLogContext ctx = RequestLogContext.from(request, env, handler);
        String body = payloadToString(payload);
        RequestResponseLogFormatter.logRequest(ctx, body, traceId);
    }

    public static void logNotification(HttpServletRequest request, Environment env, Object payload, String traceId) {
        logRequest(request, env, "NotificationClient", payload, traceId);
    }

    private static String payloadToString(Object payload) {
        if (payload == null) return "";
        if (payload instanceof String) return (String) payload;
        try {
            // Convert to compact key-value pairs instead of JSON
            if (payload instanceof java.util.Map) {
                return ((java.util.Map<?,?>)payload).entrySet().stream()
                    .map(e -> String.format("%s=%s", e.getKey(), e.getValue()))
                    .reduce((a, b) -> a + ", " + b).orElse("");
            }
            // For POJOs, use reflection to print fields as key=value
            StringBuilder sb = new StringBuilder();
            for (java.lang.reflect.Field field : payload.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(payload);
                sb.append(field.getName()).append("=").append(value).append(", ");
            }
            if (sb.length() > 2) sb.setLength(sb.length() - 2); // remove last comma
            return sb.toString();
        } catch (Exception ex) {
            return payload.toString();
        }
    }
}

class ObjectMapperHolder {
    public static final ObjectMapper INSTANCE = new ObjectMapper();
}
