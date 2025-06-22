package team.incube.gsmc.v2.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.EnableLoadTimeWeaving;

/**
 * AOP 관련 설정을 담당하는 구성 클래스입니다.
 * <p>{@code @EnableAspectJAutoProxy}를 통해 Spring AOP 프록시 기반의 Aspect 처리 기능을 활성화합니다.
 * <p>이 설정은 {@code @Aspect}가 붙은 클래스들이 올바르게 작동할 수 있도록 지원합니다.
 * <p>Spring의 컴포넌트 스캔에 의해 자동 등록되며, 별도 설정 없이도 AOP 기반 로깅, 트랜잭션 처리 등에 활용됩니다.
 * @author snowykte0426
 */
@Configuration
@EnableAspectJAutoProxy
public class AspectConfig {
}