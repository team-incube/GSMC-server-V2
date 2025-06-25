package team.incube.gsmc.v2.aspect.log;

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

/**
 * {@code HttpLoggingAspect}는 {@code @RestController}가 적용된 클래스의 메서드에 대해
 * HTTP 요청과 응답 정보를 로깅하는 AOP 클래스입니다.
 * 
 * <p><b>현재 비활성화됨:</b> 필터 기반 로깅({@code HttpLoggingFilter})으로 대체되었습니다.
 * 
 * <p>요청 시 다음의 정보를 로깅합니다:
 * <ul>
 *   <li>클라이언트 IP</li>
 *   <li>세션 ID</li>
 *   <li>요청 URI</li>
 *   <li>HTTP 메서드</li>
 *   <li>쿼리 파라미터</li>
 *   <li>Content-Type</li>
 *   <li>User-Agent</li>
 *   <li>헤더 목록</li>
 *   <li>메서드 파라미터</li>
 * </ul>
 * <p>응답 시 다음의 정보를 로깅합니다:
 * <ul>
 *   <li>클라이언트 IP</li>
 *   <li>세션 ID</li>
 *   <li>응답 헤더</li>
 *   <li>응답 본문</li>
 *   <li>HTTP 상태 코드</li>
 * </ul>
 * <p>요청과 응답 로그에는 트래킹을 위한 UUID 코드가 함께 포함됩니다.
 * <p>응답 객체가 {@code ResponseEntity}가 아닌 경우 {@code RuntimeException}을 발생시킵니다.
 * @author snowykte0426
 * @deprecated 필터 기반 로깅으로 대체됨. 이 클래스는 더 이상 사용되지 않습니다.
 */
@Aspect
// @Component  // 필터 기반 로깅으로 대체되어 비활성화
@Slf4j
@Deprecated
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