package team.incube.gsmc.v2.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 비동기 처리 관련 설정을 담당하는 구성 클래스입니다.
 * <p>{@code @EnableAsync}를 통해 Spring의 비동기 메서드 실행 기능을 활성화합니다.
 * <p>이 설정은 {@code @Async}가 붙은 메서드들이 비동기로 실행될 수 있도록 지원합니다.
 * <p>Spring의 컴포넌트 스캔에 의해 자동 등록되며, 별도 설정 없이도 비동기 작업 처리에 활용됩니다.
 * @author snowykte0426
 */
@Configuration
@EnableAsync
public class AsyncConfig {
}