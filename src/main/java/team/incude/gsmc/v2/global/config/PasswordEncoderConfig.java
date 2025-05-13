package team.incude.gsmc.v2.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 비밀번호 암호화를 위한 Spring Security 설정 클래스입니다.
 * <p>{@link BCryptPasswordEncoder}를 Spring Bean으로 등록하여 서비스 계층에서 사용할 수 있도록 제공합니다.
 * <p>BCrypt는 단방향 해시 함수로, 사용자 비밀번호를 안전하게 저장하고 검증할 수 있는 방식입니다.
 * 이 설정은 회원 가입, 로그인, 비밀번호 변경 등의 인증 로직에서 활용됩니다.
 * @author jihoonwjj
 */
@Configuration
public class PasswordEncoderConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
