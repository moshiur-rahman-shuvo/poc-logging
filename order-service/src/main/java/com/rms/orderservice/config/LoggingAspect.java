//package com.rms.orderservice.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Aspect
//@Component
//public class LoggingAspect {
////    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
//
//    @Around("execution(* com.rms.orderservice.controller..*(..)) || execution(* com.rms.orderservice.service..*(..))")
//    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();
//        Object[] args = joinPoint.getArgs();
//        log.info("Invoking method: {} with params: {}", methodName, args);
//        long start = System.currentTimeMillis();
//        Object result = joinPoint.proceed();
//        long elapsed = System.currentTimeMillis() - start;
//        log.info("Method {} executed in {} ms", methodName, elapsed);
//        return result;
//    }
//}
