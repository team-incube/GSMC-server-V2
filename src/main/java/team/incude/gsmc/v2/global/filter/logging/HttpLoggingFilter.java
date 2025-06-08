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

@Slf4j
public class HttpLoggingFilter implements Filter {

    private static final List<MediaType> VISIBLE_TYPES = Arrays.asList(
            MediaType.valueOf("text/*"),
            MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_XML,
            MediaType.valueOf("application/*+json"),
            MediaType.valueOf("application/*+xml")
    );

    private static final int MAX_PAYLOAD_LENGTH = 10000;
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초 SSS밀리초");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
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

    private ContentCachingRequestWrapper wrapRequest(ServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        }
        return new ContentCachingRequestWrapper((HttpServletRequest) request);
    }

    private ContentCachingResponseWrapper wrapResponse(ServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        }
        return new ContentCachingResponseWrapper((HttpServletResponse) response);
    }

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

    private boolean isVisible(MediaType mediaType) {
        return VISIBLE_TYPES.stream()
                .anyMatch(visibleType -> visibleType.includes(mediaType));
    }

    private String truncateIfNeeded(String content) {
        if (content == null) {
            return "null";
        }
        return content.length() <= MAX_PAYLOAD_LENGTH ? content
                : content.substring(0, MAX_PAYLOAD_LENGTH) + "... (truncated)";
    }
}