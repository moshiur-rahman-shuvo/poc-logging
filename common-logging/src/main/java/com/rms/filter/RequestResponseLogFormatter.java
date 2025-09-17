package com.rms.filter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestResponseLogFormatter {
    public static String formatRequest(String appName, String remoteAddr, String requestTime, String method, String uri, String protocol, String forwardedFor, String env, String userAgent, String threadName, String handler, String requestBody, String traceId) {
        return String.format(
            "[%s][Request] %s - [%s] \"%s %s %s\" \"%s\" \"%s\" \"%s\" \"%s\" INFO \"%s\" - [RequestBody: %s] %s",
            appName,
            remoteAddr,
            requestTime,
            method,
            uri,
            protocol,
            forwardedFor,
            env,
            userAgent,
            threadName,
            handler,
            requestBody,
            traceId
        );
    }

    public static String formatResponse(String appName, String remoteAddr, String requestTime, String method, String uri, String protocol, String forwardedFor, String env, String userAgent, String threadName, String level, String handler, String responseBody, String traceId, long durationMs) {
        return String.format(
            "[%s][Response] %s - [%s] \"%s %s %s\" \"%s\" \"%s\" \"%s\" \"%s\" %s \"%s\" - [ResponseBody: %s] %s (%d ms)",
            appName,
            remoteAddr,
            requestTime,
            method,
            uri,
            protocol,
            forwardedFor,
            env,
            userAgent,
            threadName,
            level,
            handler,
            responseBody,
            traceId,
            durationMs
        );
    }

    public static String getRequestTime() {
        return new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z").format(new Date());
    }

    public static String formatRequest(RequestLogContext ctx, String requestBody, String traceId) {
        return formatRequest(ctx.appName, ctx.remoteAddr, ctx.requestTime, ctx.method, ctx.uri, ctx.protocol, ctx.forwardedFor, ctx.env, ctx.userAgent, ctx.threadName, ctx.handler, requestBody, traceId);
    }

    public static void logRequest(RequestLogContext ctx, String requestBody, String traceId) {
        Logger logger = LoggerFactory.getLogger("REQUEST_LOGGER");
        logger.info(formatRequest(ctx, requestBody, traceId));
    }

    public static String formatResponse(RequestLogContext ctx, String responseBody, String traceId, String level, long durationMs) {
        return formatResponse(ctx.appName, ctx.remoteAddr, ctx.requestTime, ctx.method, ctx.uri, ctx.protocol, ctx.forwardedFor, ctx.env, ctx.userAgent, ctx.threadName, level, ctx.handler, responseBody, traceId, durationMs);
    }

    public static void logResponse(RequestLogContext ctx, String responseBody, String traceId, String level, long durationMs) {
        Logger logger = LoggerFactory.getLogger("REQUEST_LOGGER");
        if ("ERROR".equalsIgnoreCase(level)) {
            logger.error(formatResponse(ctx, responseBody, traceId, level, durationMs));
        } else {
            logger.info(formatResponse(ctx, responseBody, traceId, level, durationMs));
        }
    }
}
