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
 * @author jihoonwjj, snowykte0426, C0ng_yun
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtPort jwtPort;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final static List<String> EXCLUDED_PATHS = List.of(
            "/api/v2/auth/**",
            "/actuator/prometheus/**",
            "/api/v2/health/**"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return EXCLUDED_PATHS.stream()
                .anyMatch(path -> pathMatcher.match(path, request.getRequestURI()));
    }

    /**
     * 필터가 적용될 경로를 정의합니다.
     * <p>"/api/v2/auth/**", "/actuator/prometheus/**", "/api/v2/health/**" 경로는 인증을 건너뜁니다.
     * @param request HTTP 요청
     * @return true if the filter should be applied, false otherwise
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = jwtPort.resolveToken(request);
        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }
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