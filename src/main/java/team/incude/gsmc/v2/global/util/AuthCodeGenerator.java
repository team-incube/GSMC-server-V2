package team.incude.gsmc.v2.global.util;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

/**
 * 이메일 인증을 위한 인증 코드를 생성하는 유틸리티 클래스입니다.
 * <p>8자리 숫자로 구성된 무작위 인증 코드를 생성하며, 인증 요청 시 코드 발급에 사용됩니다.
 * <p>{@link SecureRandom}을 사용하여 예측 불가능한 안전한 숫자를 생성합니다.
 * 이 클래스는 {@code @UtilityClass}로 선언되어 정적 메서드만 포함합니다.
 * @author jihoonwjj
 */
@UtilityClass
public class AuthCodeGenerator {

    public String generateAuthCode() {
        return String.valueOf(10000000 + new SecureRandom().nextInt(90000000));
    }
}
