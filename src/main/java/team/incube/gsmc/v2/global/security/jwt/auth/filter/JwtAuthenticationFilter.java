package team.incube.gsmc.v2.global.security.jwt.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import team.incube.gsmc.v2.domain.auth.application.port.JwtPort;
import team.incube.gsmc.v2.domain.member.domain.constant.MemberRole;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * JWT 인증 필터 클래스입니다.
 * <p>HTTP 요청에서 JWT 토큰을 추출하고, 유효성을 검사하여 인증 정보를 설정합니다.
 * <p>특정 경로에 대해서는 인증을 건너뛰며, 유효하지 않은 토큰에 대해서는 401 Unauthorized 응답을 반환합니다.
 * @author jihoonwjj, snowykte0426, Jeongjunyun777
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtPort jwtPort;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 건너뛰는 경로 목록
     * <p>다음 경로들은 JWT 토큰 검증 없이 접근할 수 있습니다:
     * <p>
     * • /api/v2/auth/** - 인증 관련 API<br>
     * • /actuator/prometheus/** - 모니터링 메트릭 엔드포인트<br>
     * • /api/v2/health/** - 헬스체크 API<br>
     * • /swagger-ui/** - Swagger UI 리소스<br>
     * • /v3/api-docs/** - OpenAPI 문서<br>
     * • /swagger-ui.html - Swagger UI 메인 페이지
     */
    private final static List<String> EXCLUDED_PATHS = List.of(
            "/api/v2/auth/**",
            "/actuator/prometheus/**",
            "/api/v2/health/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html"
    );

    /**
     * 요청된 경로가 JWT 인증 필터를 건너뛸지 결정합니다.
     * <p>EXCLUDED_PATHS에 정의된 경로 패턴과 일치하는 요청은 JWT 토큰 검증을 수행하지 않습니다.
     * "/api/v2/auth/**", "/actuator/prometheus/**", "/api/v2/health/**", 
     * "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html" 경로는 인증을 건너뜁니다.
     *
     * @param request HTTP 요청 객체
     * @return 필터를 건너뛸 경우 true, 필터를 적용할 경우 false
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return EXCLUDED_PATHS.stream()
                .anyMatch(path -> pathMatcher.match(path, request.getRequestURI()));
    }

    /**
     * JWT 토큰을 검증하고 인증 정보를 설정하는 필터 로직을 수행합니다.
     * <p>유효한 JWT 토큰이 있는 경우 SecurityContext에 인증 정보를 설정하고,
     * 토큰이 없거나 유효하지 않은 경우 401 Unauthorized 응답을 반환합니다.
     *
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param filterChain 필터 체인
     * @throws ServletException 서블릿 예외 발생 시
     * @throws IOException I/O 예외 발생 시
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = jwtPort.resolveToken(request);
        if (token != null && jwtPort.validateAccessToken(token)) {
            String email = jwtPort.getEmailFromAccessToken(token);
            MemberRole roles = jwtPort.getRolesFromAccessToken(token);
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(roles.name()));
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
            return;
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"Unauthorized or invalid token.\"}");
    }
}