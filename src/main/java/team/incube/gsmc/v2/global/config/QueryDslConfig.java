package team.incube.gsmc.v2.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * QueryDSL 사용을 위한 설정 클래스입니다.
 * <p>{@link JPAQueryFactory}를 Spring Bean으로 등록하여 영속 계층에서 주입받아 사용할 수 있도록 구성합니다.
 * <p>{@link EntityManager}는 {@code @PersistenceContext}를 통해 주입되며,
 * JPAQueryFactory는 이를 기반으로 쿼리를 생성하고 실행할 수 있도록 지원합니다.
 * 이 설정을 통해 타입 안전한 쿼리 작성을 가능하게 하며, QueryDSL 기반의 동적 쿼리 구현에 활용됩니다.
 * @author snowykte0426
 */
@Configuration
public class QueryDslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    protected JPAQueryFactory queryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}