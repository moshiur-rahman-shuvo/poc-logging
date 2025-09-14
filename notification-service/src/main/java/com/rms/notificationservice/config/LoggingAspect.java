package com.rms.notificationservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
//    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("execution(* com.rms.notificationservice.controller..*(..)) || execution(* com.rms.notificationservice.service..*(..))")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();
        Object[] args = joinPoint.getArgs();
        String paramsJson;
        try {
            paramsJson = objectMapper.writeValueAsString(args);
        } catch (Exception e) {
            paramsJson = "[unserializable params]";
        }
        log.info("Invoking method: {} with params: {}", methodName, paramsJson);
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long elapsed = System.currentTimeMillis() - start;
        log.info("Method {} executed in {} ms", methodName, elapsed);
        return result;
    }
}

