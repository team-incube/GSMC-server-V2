package team.incude.gsmc.v2.global.security.jwt.auth.filter;

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
import team.incude.gsmc.v2.domain.member.domain.constant.MemberRole;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.JwtParserUseCase;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final JwtParserUseCase jwtParserUseCase;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = jwtParserUseCase.resolveToken(request);
        String uri = request.getRequestURI();
        if (uri.startsWith("/api/v2/auth") || uri.startsWith("/actuator/prometheus") || uri.startsWith("/api/v2/health")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (token != null && jwtParserUseCase.validateAccessToken(token)) {
            String email = jwtParserUseCase.getEmailFromAccessToken(token);
            MemberRole roles = jwtParserUseCase.getRolesFromAccessToken(token);
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