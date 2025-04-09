package team.incude.gsmc.v2.domain.auth.application.port;

import team.incude.gsmc.v2.domain.auth.domain.AuthCode;

public interface AuthCodePersistencePort {

    Boolean existsAuthCodeByCode(String code);

    AuthCode findAuthCodeByCode(String code);

    void saveAuthCode(AuthCode authCode);

    void deleteAuthCodeByCode(String code);
}
