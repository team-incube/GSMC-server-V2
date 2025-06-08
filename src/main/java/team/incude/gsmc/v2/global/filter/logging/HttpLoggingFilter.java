package team.incude.gsmc.v2.global.filter.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * HTTP 요청과 응답을 로깅하는 필터입니다.
 * 요청과 응답의 Header, Body, Status 등을 로그로 남깁니다.
 * 특정 URL 패턴은 제외할 수 있습니다.
 * <p>이 필터는 다음과 같은 정보를 로그로 남깁니다:</p>
 * <ul>
 *   <li>요청/응답 시간 (한국식 날짜 형식)</li>
 *   <li>클라이언트 IP</li>
 *   <li>세션 ID</li>
 *   <li>HTTP 메서드 및 URI</li>
 *   <li>쿼리 파라미터</li>
 *   <li>요청/응답 헤더</li>
 *   <li>요청/응답 본문 (제한된 길이)</li>
 *   <li>응답 상태 코드 및 처리 시간</li>
 * </ul>
 * <p>와일드카드 패턴을 사용하여 특정 경로를 로깅에서 제외할 수 있습니다:</p>
 * <ul>
 *   <li><code>/path/**</code> - /path로 시작하는 모든 하위 경로 제외</li>
 *   <li><code>/path/*</code> - /path의 직접적인 하위 경로만 제외</li>
 *   <li><code>/exact/path</code> - 정확히 일치하는 경로만 제외</li>
 * </ul>
 * @author jihoonwjj, snowykte0426
 */
@Slf4j
public class HttpLoggingFilter implements Filter {

    /**
     * 로깅할 수 있는 미디어 타입 목록입니다.
     * 이 목록에 포함되지 않은 타입은 바이너리로 간주되어 로깅하지 않습니다.
     */
    private static final List<MediaType> VISIBLE_TYPES = Arrays.asList(
            MediaType.valueOf("text/*"),
            MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_XML,
            MediaType.valueOf("application/*+json"),
            MediaType.valueOf("application/*+xml")
    );

    /**
     * 로그에 포함되는 최대 본문 길이입니다.
     * 이 길이를 초과하는 내용은 잘려서 표시됩니다.
     */
    private static final int MAX_PAYLOAD_LENGTH = 10000;
    /**
     * 로그에 표시되는 날짜/시간 형식입니다.
     * 한국식 날짜 표기 형식으로 밀리초까지 포함합니다.
     */
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초 SSS밀리초");

    /**
     * 로깅에서 제외할 URL 패턴 목록입니다.
     */
    private final List<String> excludePatterns;

    /**
     * 사용자 정의 제외 패턴을 사용하여 필터를 생성합니다.
     * @param excludePatterns 로깅에서 제외할 URL 패턴 목록
     */
    public HttpLoggingFilter(List<String> excludePatterns) {
        this.excludePatterns = excludePatterns != null ? excludePatterns : Collections.emptyList();
    }

    /**
     * 기본 제외 패턴을 사용하여 필터를 생성합니다.
     */
    public HttpLoggingFilter() {
        this.excludePatterns = Arrays.asList(
                "/actuator/**"
        );
    }

    /**
     * HTTP 요청과 응답을 로깅하는 필터 메서드입니다.
     * <p>이 메서드는 다음과 같은 작업을 수행합니다:</p>
     * <ol>
     *   <li>요청과 응답이 HTTP 타입인지 확인</li>
     *   <li>제외 패턴에 해당하는 URL인지 확인</li>
     *   <li>요청과 응답을 캐싱 래퍼로 감싸기</li>
     *   <li>고유 식별 코드 생성 및 시작 시간 기록</li>
     *   <li>필터 체인 실행</li>
     *   <li>요청과 응답 정보 로깅</li>
     * </ol>
     * @param request 서블릿 요청
     * @param response 서블릿 응답
     * @param chain 필터 체인
     * @throws IOException I/O 예외 발생 시
     * @throws ServletException 서블릿 예외 발생 시
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest httpRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        String requestURI = httpRequest.getRequestURI();

        if (shouldExclude(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper requestWrapper = wrapRequest(request);
        ContentCachingResponseWrapper responseWrapper = wrapResponse(response);

        UUID code = UUID.randomUUID();
        long startTime = System.currentTimeMillis();

        try {
            chain.doFilter(requestWrapper, responseWrapper);
        } finally {
            try {
                logRequest(requestWrapper, code, startTime);
                logResponse(responseWrapper, requestWrapper, code, startTime);
            } catch (Exception e) {
                log.error("Logging failed", e);
            } finally {
                responseWrapper.copyBodyToResponse();
            }
        }
    }

    /**
     * 주어진 URL이 로깅에서 제외되어야 하는지 확인합니다.
     * @param requestURI 확인할 요청 URI
     * @return 제외해야 할 경우 true, 그렇지 않으면 false
     */
    private boolean shouldExclude(String requestURI) {
        return excludePatterns.stream()
                .anyMatch(pattern -> pathMatches(pattern, requestURI));
    }

    /**
     * URL 패턴이 주어진 경로와 일치하는지 확인합니다.
     * 다음과 같은 패턴을 지원합니다:
     * <ul>
     *   <li><code>/path/**</code> - /path로 시작하는 모든 하위 경로</li>
     *   <li><code>/path/*</code> - /path의 직접적인 하위 경로만</li>
     *   <li><code>/exact/path</code> - 정확히 일치하는 경로</li>
     * </ul>
     * @param pattern 확인할 패턴
     * @param path 확인할 경로
     * @return 패턴과 경로가 일치하면 true, 그렇지 않으면 false
     */
    private boolean pathMatches(String pattern, String path) {
        if (pattern.endsWith("/**")) {
            String prefix = pattern.substring(0, pattern.length() - 3);
            return path.startsWith(prefix);
        } else if (pattern.endsWith("/*")) {
            String prefix = pattern.substring(0, pattern.length() - 2);
            int slashCount = prefix.length() - prefix.replace("/", "").length();
            int pathSlashCount = path.length() - path.replace("/", "").length();
            return path.startsWith(prefix) && pathSlashCount == slashCount;
        } else {
            return path.equals(pattern);
        }
    }

    /**
     * 서블릿 요청을 ContentCachingRequestWrapper로 감싸서 반환합니다.
     * 이미 ContentCachingRequestWrapper인 경우 그대로 반환합니다.
     * @param request 원본 서블릿 요청
     * @return ContentCachingRequestWrapper로 감싼 요청
     */
    private ContentCachingRequestWrapper wrapRequest(ServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        }
        return new ContentCachingRequestWrapper((HttpServletRequest) request);
    }

    /**
     * 서블릿 응답을 ContentCachingResponseWrapper로 감싸서 반환합니다.
     * 이미 ContentCachingResponseWrapper인 경우 그대로 반환합니다.
     * @param response 원본 서블릿 응답
     * @return ContentCachingResponseWrapper로 감싼 응답
     */
    private ContentCachingResponseWrapper wrapResponse(ServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        }
        return new ContentCachingResponseWrapper((HttpServletResponse) response);
    }

    /**
     * HTTP 요청 정보를 로그로 기록합니다.
     * @param request 요청 래퍼
     * @param code 고유 식별 코드
     * @param startTime 요청 시작 시간
     */
    private void logRequest(ContentCachingRequestWrapper request, UUID code, long startTime) {
        String ip = request.getRemoteAddr();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String sessionId = request.getRequestedSessionId();

        String formattedDate = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(startTime),
                ZoneId.systemDefault()
        ).format(DATE_FORMATTER);

        Map<String, String> headers = new HashMap<>();
        Collections.list(request.getHeaderNames()).forEach(headerName ->
                headers.put(headerName, request.getHeader(headerName))
        );

        String requestBody = getRequestBody(request);

        log.info("[REQUEST] Code: {} | Time: {} | IP: {} | Session: {} | {} {} | Query: {} | Headers: {} | Body: {}",
                code,
                formattedDate,
                ip,
                StringUtils.hasText(sessionId) ? sessionId : "No Session",
                method,
                uri,
                StringUtils.hasText(queryString) ? queryString : "No Query String",
                headers,
                requestBody
        );
    }

    /**
     * HTTP 응답 정보를 로그로 기록합니다.
     * @param response 응답 래퍼
     * @param request 원본 HTTP 요청
     * @param code 고유 식별 코드
     * @param startTime 요청 시작 시간
     */
    private void logResponse(ContentCachingResponseWrapper response, HttpServletRequest request, UUID code, long startTime) {
        String ip = request.getRemoteAddr();
        int status = response.getStatus();
        long duration = System.currentTimeMillis() - startTime;

        String formattedDate = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(System.currentTimeMillis()),
                ZoneId.systemDefault()
        ).format(DATE_FORMATTER);

        Map<String, String> responseHeaders = new HashMap<>();
        response.getHeaderNames().forEach(headerName ->
                responseHeaders.put(headerName, response.getHeader(headerName))
        );

        String responseBody = getResponseBody(response);

        log.info("[RESPONSE] Code: {} | IP: {} | Time: {} | Status: {} | Duration: {}ms | Headers: {} | Body: {}",
                code,
                ip,
                formattedDate,
                status,
                duration,
                responseHeaders,
                truncateIfNeeded(responseBody)
        );
    }

    /**
     * HTTP 요청 본문을 가져옵니다.
     * 콘텐츠 타입에 따라 적절하게 처리하며, 바이너리 콘텐츠는 로깅하지 않습니다.
     * @param request 요청 래퍼
     * @return 요청 본문 문자열 또는 상태 메시지
     */
    private String getRequestBody(ContentCachingRequestWrapper request) {
        String contentType = request.getContentType();
        if (contentType == null) {
            return "No Content Type";
        }

        byte[] content = request.getContentAsByteArray();
        if (content.length == 0) {
            return "Empty Body";
        }

        try {
            MediaType mediaType = MediaType.valueOf(contentType);
            if (!isVisible(mediaType)) {
                return "[Binary Content]";
            }
            return truncateIfNeeded(new String(content, StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.warn("Failed to read request body", e);
            return "[Error reading request body]";
        }
    }

    /**
     * HTTP 응답 본문을 가져옵니다.
     * 콘텐츠 타입에 따라 적절하게 처리하며, 바이너리 콘텐츠는 로깅하지 않습니다.
     * @param response 응답 래퍼
     * @return 응답 본문 문자열 또는 상태 메시지
     */
    private String getResponseBody(ContentCachingResponseWrapper response) {
        String contentType = response.getContentType();
        if (contentType == null) {
            return "No Content Type";
        }

        byte[] content = response.getContentAsByteArray();
        if (content.length == 0) {
            return "Empty Body";
        }

        try {
            MediaType mediaType = MediaType.valueOf(contentType);
            if (!isVisible(mediaType)) {
                return "[Binary Content]";
            }
            return truncateIfNeeded(new String(content, StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.warn("Failed to read response body", e);
            return "[Error reading response body]";
        }
    }

    /**
     * 주어진 MediaType이 로깅 가능한 타입인지 확인합니다.
     * @param mediaType 확인할 미디어 타입
     * @return 로깅 가능한 타입이면 true, 그렇지 않으면 false
     */
    private boolean isVisible(MediaType mediaType) {
        return VISIBLE_TYPES.stream()
                .anyMatch(visibleType -> visibleType.includes(mediaType));
    }

    /**
     * 콘텐츠 길이가 최대 길이를 초과하는 경우 잘라냅니다.
     * @param content 원본 콘텐츠 문자열
     * @return 필요한 경우 잘린 콘텐츠 문자열
     */
    private String truncateIfNeeded(String content) {
        if (content == null) {
            return "null";
        }
        return content.length() <= MAX_PAYLOAD_LENGTH ? content
                : content.substring(0, MAX_PAYLOAD_LENGTH) + "... (truncated)";
    }
}