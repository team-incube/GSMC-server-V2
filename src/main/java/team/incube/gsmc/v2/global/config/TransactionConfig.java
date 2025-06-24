package team.incube.gsmc.v2.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring의 선언적 트랜잭션 관리를 활성화하기 위한 설정 클래스입니다.
 * <p>{@link EnableTransactionManagement}를 통해 {@code @Transactional} 어노테이션이 적용된 메서드에서 트랜잭션 처리를 가능하게 합니다.
 * <p>이 클래스 자체에는 추가 설정이 없지만, 추후 PlatformTransactionManager 설정을 확장하거나 커스터마이징할 때 기반이 됩니다.
 * @author snowykte0426
 */
@Configuration
@EnableTransactionManagement
public class TransactionConfig {
}