package team.incude.gsmc.v2.global.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.TimeZone;

/**
 * 애플리케이션의 기본 시간대를 설정하는 구성 클래스입니다.
 * <p>서버 실행 시 {@link PostConstruct}를 통해 JVM의 기본 시간대를 "Asia/Seoul"로 설정합니다.
 * {@link System#setProperty(String, String)} 및 {@link TimeZone#setDefault(TimeZone)}를 사용하여 전체 애플리케이션에 적용됩니다.
 * <p>로깅을 통해 설정 결과를 출력하며, 설정 실패 시 경고 로그를 남깁니다.
 * 이 설정은 시간 관련 연산(LocalDateTime 등)에서 일관된 시간대를 보장하기 위해 사용됩니다.
 * @author snowykte0426
 */
@Slf4j
@Configuration
public class TimeZoneConfig {

    @PostConstruct
    public void init() {
        try {
            System.setProperty("user.timezone", "Asia/Seoul");
            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
            log.info("Default timezone set to Asia/Seoul: {}", LocalDateTime.now());
        } catch (Exception e) {
            log.warn("Failed to set default timezone: ", e);
        }
    }
}