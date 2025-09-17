//package com.rms.filter;
//
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.slf4j.MDC;
//import org.springframework.stereotype.Component;
//import org.springframework.web.util.ContentCachingRequestWrapper;
//import org.springframework.web.util.ContentCachingResponseWrapper;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.UUID;
//
//@Component
//public class RequestResponseLoggingFilter implements Filter {
//
//    private static final Logger log = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);
//    private static final String CORRELATION_ID = "correlationId";
//    private static final String ENV = "prod"; // Change if needed
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest httpReq = (HttpServletRequest) request;
//        HttpServletResponse httpRes = (HttpServletResponse) response;
//
//        // Wrap request/response to read body later
//        ContentCachingRequestWrapper reqWrapper = new ContentCachingRequestWrapper(httpReq);
//        ContentCachingResponseWrapper resWrapper = new ContentCachingResponseWrapper(httpRes);
//
//        // Generate correlation ID and add to MDC
//        String correlationId = UUID.randomUUID().toString().replace("-", "");
//        MDC.put(CORRELATION_ID, correlationId);
//
//        long start = System.currentTimeMillis();
//        try {
//            chain.doFilter(reqWrapper, resWrapper);
//        } finally {
//            long duration = System.currentTimeMillis() - start;
//
//            logRequest(reqWrapper, correlationId);
//            logResponse(reqWrapper, resWrapper, correlationId, duration);
//
//            // Important: copy response back to client
//            resWrapper.copyBodyToResponse();
//
//            // Clean MDC
//            MDC.remove(CORRELATION_ID);
//        }
//    }
//
//    private void logRequest(ContentCachingRequestWrapper request, String correlationId) {
//        String clientIp = request.getRemoteAddr();
//        String time = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z").format(new Date());
//        String method = request.getMethod();
//        String uri = request.getRequestURI();
//        String protocol = request.getProtocol();
//        String forwardedFor = request.getHeader("X-Forwarded-For");
//        String userAgent = request.getHeader("User-Agent");
//
//        log.info("\n[Request]\n\n{} - [{}] \"{} {} {}\" \"{}\" \"{}\" \"{}\" \"{}\" INFO \"{}\" - [RequestBody: {}] {}",
//                clientIp, time, method, uri, protocol,
//                forwardedFor, ENV, userAgent, Thread.currentThread().getName(),
//                request.getClass().getSimpleName(),
//                new String(request.getContentAsByteArray()), correlationId
//        );
//    }
//
//    private void logResponse(ContentCachingRequestWrapper request,
//                             ContentCachingResponseWrapper response,
//                             String correlationId, long duration) {
//        String clientIp = request.getRemoteAddr();
//        String time = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z").format(new Date());
//        String method = request.getMethod();
//        String uri = request.getRequestURI();
//        String protocol = request.getProtocol();
//        String forwardedFor = request.getHeader("X-Forwarded-For");
//        String userAgent = request.getHeader("User-Agent");
//
//        log.info("\n[Response]\n\n{} - [{}] \"{} {} {}\" \"{}\" \"{}\" \"{}\" \"{}\" INFO \"{}\" - [ResponseBody: {}] {} ({} ms)",
//                clientIp, time, method, uri, protocol,
//                forwardedFor, ENV, userAgent, Thread.currentThread().getName(),
//                response.getClass().getSimpleName(),
//                new String(response.getContentAsByteArray()), correlationId, duration
//        );
//    }
//}
//
