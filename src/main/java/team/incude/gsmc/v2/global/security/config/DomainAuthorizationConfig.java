package team.incude.gsmc.v2.global.security.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;

import team.incude.gsmc.v2.domain.member.domain.constant.MemberRole;

@Component
public class DomainAuthorizationConfig {

    public void configureAuthorization(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                // /api/v2/members
                .requestMatchers(HttpMethod.GET, 
                    "api/v2/members/students",
                    "api/v2/members/students/search"
                )
                .hasAnyAuthority(
                    MemberRole.ROLE_ADMIN.name()
                )
                .requestMatchers(HttpMethod.GET, "/api/v2/members/students/current")
                .hasAnyAuthority(
                    MemberRole.ROLE_STUDENT.name()
                )

                // /api/v2/certificates
                .requestMatchers(HttpMethod.GET, "/api/v2/certificates/current")
                .hasAnyAuthority(
                    MemberRole.ROLE_STUDENT.name()
                )
                .requestMatchers(HttpMethod.GET, "/api/v2/certificates/{email}")
                .hasAnyAuthority(
                    MemberRole.ROLE_ADMIN.name()
                )
                .requestMatchers(HttpMethod.DELETE, "/api/v2/certificates/current/{certificateId}")
                .hasAnyAuthority(
                    MemberRole.ROLE_STUDENT.name()
                )
                .requestMatchers(HttpMethod.DELETE, "/api/v2/certificates/{email}/{certificateId}")
                .hasAnyAuthority(
                    MemberRole.ROLE_ADMIN.name()
                )
                .requestMatchers(HttpMethod.PATCH, "/api/v2/certificates/current/{certificateId}")
                .hasAnyAuthority(
                    MemberRole.ROLE_STUDENT.name()
                )

                // /api/v2/score
                .requestMatchers(HttpMethod.GET, "/api/v2/score/current")
                .hasAnyAuthority(
                    MemberRole.ROLE_STUDENT.name()
                )
                .requestMatchers(HttpMethod.GET, "/api/v2/score/{studentCode}")
                .hasAnyAuthority(
                    MemberRole.ROLE_ADMIN.name()
                )
                .requestMatchers(HttpMethod.PATCH, "/api/v2/score/current")
                .hasAnyAuthority(
                    MemberRole.ROLE_STUDENT.name()
                )
                .requestMatchers(HttpMethod.PATCH, "/api/v2/score/{studentCode}")
                .hasAnyAuthority(
                    MemberRole.ROLE_ADMIN.name()
                )

                // /api/v2/evidence
                .requestMatchers(HttpMethod.GET, 
                    "/api/v2/evidence/current",
                    "/api/v2/evidence/search"
                )
                .hasAnyAuthority(
                    MemberRole.ROLE_STUDENT.name()
                )
                .requestMatchers(HttpMethod.GET, "/api/v2/evidence/{studentCode}")
                .hasAnyAuthority(
                    MemberRole.ROLE_ADMIN.name()
                )
                .requestMatchers(HttpMethod.POST, 
                    "/api/v2/evidence/current/activity",
                    "/api/v2/evidence/current/reading",
                    "/api/v2/evidence/current/other",
                    "/api/v2/evidence/current/scoring"
                )
                .hasAnyAuthority(
                    MemberRole.ROLE_STUDENT.name()
                )
                .requestMatchers(HttpMethod.PATCH, "/api/v2/evidence/{evidenceId}/status")
                .hasAnyAuthority(
                    MemberRole.ROLE_ADMIN.name()
                )
                .requestMatchers(HttpMethod.DELETE, "/api/v2/evidence/current/{evidenceId}")
                .hasAnyAuthority(
                    MemberRole.ROLE_STUDENT.name()
                )
                .requestMatchers(HttpMethod.POST, 
                    "api/v2/evidence/current/draft/activity",
                    "/api/v2/evidence/current/draft/reading"
                )
                .hasAnyAuthority(
                    MemberRole.ROLE_STUDENT.name()
                )
                .requestMatchers(HttpMethod.GET,
                    "/api/v2/evidence/draft/activity/{draftId}",
                    "/api/v2/evidence/draft/reading/{draftId}",
                    "/api/v2/evidence/current/draft"
                )
                .hasAnyAuthority(
                    MemberRole.ROLE_STUDENT.name()
                )

                // /api/v2/sheet
                .requestMatchers(HttpMethod.GET, "/api/v2/sheet/{grade}/{classNumber}")
                .hasAnyAuthority(
                    MemberRole.ROLE_ADMIN.name()
                )

                .anyRequest().permitAll()
        );
    }
}