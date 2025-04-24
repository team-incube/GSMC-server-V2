package team.incude.gsmc.v2.global.security.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;

@Component
public class DomainAuthorizationConfig {

    public void configureAuthorization(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v2/auth/**").permitAll()
                .requestMatchers("/actuator/prometheus").permitAll()
                .requestMatchers("/api/v2/**").hasAnyRole("STUDENT", "ADMIN", "TEACHER")
                .anyRequest().permitAll()
        );
    }
}