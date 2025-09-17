//package com.rms.orderservice.config;
//
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import org.slf4j.MDC;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.UUID;
//
//@Component
//public class TraceIdFilter implements Filter {
//    private static final String TRACE_ID_HEADER = "trace-id";
//    private static final String MDC_KEY = "traceId";
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        try {
//            if (request instanceof HttpServletRequest) {
//                HttpServletRequest httpRequest = (HttpServletRequest) request;
//                String traceId = httpRequest.getHeader(TRACE_ID_HEADER);
//                if (traceId == null || traceId.isEmpty()) {
//                    traceId = UUID.randomUUID().toString();
//                }
//                MDC.put(MDC_KEY, traceId);
//            }
//            chain.doFilter(request, response);
//        } finally {
//            MDC.remove(MDC_KEY);
//        }
//    }
//}
//
