package team.incube.gsmc.v2.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

import java.io.IOException;

/**
 * 애플리케이션 전역에서 사용할 Retry 설정 클래스입니다.
 * <p>Spring Retry를 사용하여 특정 예외 발생 시 재시도 로직을 구성합니다.
 * <p>{@link RetryTemplate}을 빈으로 등록하여, 최대 3번의 재시도와 1초의 고정 백오프를 설정합니다.
 * 이 설정은 주로 외부 API 호출이나 데이터베이스 작업 등에서 일시적인 오류를 처리하기 위해 사용됩니다.
 * @author suuuuuuminnnnnn
 */
@Configuration
public class RetryConfig {

    @Bean
    public RetryTemplate retryTemplate() {
        return RetryTemplate.builder()
                .maxAttempts(3)
                .fixedBackoff(1000)
                .retryOn(IOException.class)
                .build();
    }
}
