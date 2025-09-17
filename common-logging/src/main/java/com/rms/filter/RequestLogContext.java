//package com.rms.filter;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.core.env.Environment;
//
//public class RequestLogContext {
//    public final String appName;
//    public final String remoteAddr;
//    public final String requestTime;
//    public final String method;
//    public final String uri;
//    public final String protocol;
//    public final String forwardedFor;
//    public final String env;
//    public final String userAgent;
//    public final String threadName;
//    public final String handler;
//
//    public RequestLogContext(String appName, String remoteAddr, String requestTime, String method, String uri, String protocol, String forwardedFor, String env, String userAgent, String threadName, String handler) {
//        this.appName = appName;
//        this.remoteAddr = remoteAddr;
//        this.requestTime = requestTime;
//        this.method = method;
//        this.uri = uri;
//        this.protocol = protocol;
//        this.forwardedFor = forwardedFor;
//        this.env = env;
//        this.userAgent = userAgent;
//        this.threadName = threadName;
//        this.handler = handler;
//    }
//
//    public static String getRequestTime() {
//        return new java.text.SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z").format(new java.util.Date());
//    }
//
//    public static RequestLogContext from(HttpServletRequest request, Environment environment, String handler) {
//        String appName = environment.getProperty("spring.application.name", "unknown-service");
//        String remoteAddr = request.getHeader("X-Forwarded-For") != null ? request.getHeader("X-Forwarded-For") : request.getRemoteAddr();
//        String requestTime = getRequestTime();
//        String method = request.getMethod();
//        String uri = request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
//        String protocol = request.getProtocol();
//        String forwardedFor = request.getHeader("X-Forwarded-For") != null ? request.getHeader("X-Forwarded-For") : "";
//        String env = "prod";
//        String userAgent = request.getHeader("User-Agent") != null ? request.getHeader("User-Agent") : "";
//        String threadName = Thread.currentThread().getName();
//        return new RequestLogContext(appName, remoteAddr, requestTime, method, uri, protocol, forwardedFor, env, userAgent, threadName, handler);
//    }
//}
