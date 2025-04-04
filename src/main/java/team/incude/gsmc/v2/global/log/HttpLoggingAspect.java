package team.incude.gsmc.v2.global.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.UUID;

@Aspect
@Component
@Slf4j
public class HttpLoggingAspect {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void onRequest() {
    }

    @Around("onRequest()")
    public Object loggingRequest(ProceedingJoinPoint pjp) throws Throwable {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        String ip = attributes.getRequest().getRemoteAddr();
        String method = attributes.getRequest().getMethod();
        String uri = attributes.getRequest().getRequestURI();
        String sessionId = attributes.getRequest().getRequestedSessionId();
        String params = attributes.getRequest().getQueryString();
        params = (params == null) ? "null" : params;
        String contentType = attributes.getRequest().getContentType();
        contentType = (contentType == null) ? "null" : contentType;
        String userAgent = attributes.getRequest().getHeader("User-Agent");
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        Enumeration<String> headerNamesEnum = attributes.getRequest().getHeaderNames();
        StringBuilder headerSet = new StringBuilder();
        while (headerNamesEnum.hasMoreElements()) {
            String headerName = headerNamesEnum.nextElement();
            headerSet.append(headerName).append(": ").append(attributes.getRequest().getHeader(headerName)).append(", ");
        }

        CodeSignature codeSignature = (CodeSignature) pjp.getSignature();
        String[] parameterNames = codeSignature.getParameterNames();
        Object[] parameterValues = pjp.getArgs();
        HashMap<String, Object> parameterMap = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            parameterMap.put(parameterNames[i], parameterValues[i]);
        }
        if (parameterMap.isEmpty()) {
            parameterMap.put("null", "null");
        }

        UUID code = UUID.randomUUID();
        log.info("Request: {} #{} [Request: {}] IP: {}, Session: {}, URI: {}, Params: {}, Content-Type: {}, User-Agent: {}, Headers: {}, Parameters: {}, Code: {}",
                className, methodName, method, ip, sessionId, uri, params, contentType, userAgent, headerSet, parameterMap, code);

        Object result = pjp.proceed();
        if (result instanceof ResponseEntity<?> responseEntity) {
            log.info("At {}#{} [Response:{}] IP: {}, Session-ID: {}, Headers: {}, Response: {}, Status-Code: {}, Code: {}",
                    className, methodName, method, ip, sessionId, responseEntity.getHeaders(), responseEntity.getBody(), responseEntity.getStatusCode(), code);
        } else if (result == null) {
            log.info("At {}#{} [Response: null] IP:{}, Session-ID: {}, Code: {}", className, methodName, ip, sessionId, code);
        } else {
            throw new RuntimeException("Unexpected response type: " + result.getClass().getName());
        }
        return result;
    }
}