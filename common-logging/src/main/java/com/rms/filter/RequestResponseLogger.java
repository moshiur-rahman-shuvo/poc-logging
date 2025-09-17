//package com.rms.filter;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.core.env.Environment;
//import org.slf4j.LoggerFactory;
//
//public class RequestResponseLogger {
//    public static void logRequest(HttpServletRequest request, Environment env, String handler, Object payload, String traceId) {
//        RequestLogContext ctx = RequestLogContext.from(request, env, handler);
//        // Print the request body as it is, no formatting
//        String body = (payload == null) ? "" : payload.toString();
//        String serviceName = ctx.appName;
//        // Log the handler name directly as className
//        String className = (handler != null && !handler.isEmpty()) ? handler : "";
//        String httpMethod = ctx.method;
//        String api = ctx.uri;
//        String logLevel = "INFO";
//        long roundTripTime = 0; // Not available for request-only log
//        String timestamp = ctx.requestTime;
//        String requestBody = body;
//        String responseBody = "";
//        String combinedLog = RequestResponseLogFormatter.formatCombined(
//            serviceName,
//            className,
//            httpMethod,
//            api,
//            logLevel,
//            traceId,
//            roundTripTime,
//            timestamp,
//            requestBody,
//            responseBody
//        );
//        LoggerFactory.getLogger("REQUEST_LOGGER").info(combinedLog);
//    }
//}
