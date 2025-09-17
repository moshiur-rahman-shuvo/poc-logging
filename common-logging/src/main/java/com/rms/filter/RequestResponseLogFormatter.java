//package com.rms.filter;
//
//public class RequestResponseLogFormatter {
//    // Only keep the new combined log format method
//    public static String formatCombined(String serviceName, String className, String httpMethod, String api, String logLevel, String traceId, long roundTripTime, String timestamp, String requestBody, String responseBody) {
//        return String.format(
//                "%s %s \"%s %s\" %s %s %d [%s] [%s] [%s]",
//                serviceName,
//                className,
//                httpMethod,
//                api,
//                logLevel,
//                traceId,
//                roundTripTime,
//                timestamp,
//                requestBody,
//                responseBody
//        );
//    }
//}
