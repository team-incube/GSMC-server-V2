package team.incude.gsmc.v2.domain.auth.application.port;

import team.incude.gsmc.v2.domain.auth.domain.Authentication;

public interface AuthenticationPersistencePort {
    Boolean existsAuthenticationByEmail(String email);

    Authentication findAuthenticationByEmail(String email);

    Authentication saveAuthentication(Authentication authentication);
}
