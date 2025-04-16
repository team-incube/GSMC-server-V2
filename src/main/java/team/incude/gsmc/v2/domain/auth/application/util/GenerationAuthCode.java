package team.incude.gsmc.v2.domain.auth.application.util;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

@UtilityClass
public class GenerationAuthCode {

    public String generateAuthCode() {
        return String.valueOf(10000000 + new SecureRandom().nextInt(90000000));
    }
}
