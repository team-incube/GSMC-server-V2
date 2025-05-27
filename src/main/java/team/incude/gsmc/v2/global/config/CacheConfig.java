package team.incude.gsmc.v2.global.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 애플리케이션 전역에서 사용할 Caffeine 기반 캐시 설정 클래스입니다.
 * <p>{@link EnableCaching}을 통해 Spring의 캐시 기능을 활성화하며,
 * {@link CaffeineCacheManager}를 빈으로 등록하여 특정 캐시 이름("categories")에 대한 설정을 구성합니다.
 * <p>캐시 만료 시간은 {@code spring.cache.expire-after-write} 프로퍼티를 통해 외부에서 주입받으며,
 * 최대 캐시 크기는 50개로 제한됩니다.
 * 이 설정은 주로 조회 성능 향상과 DB 부하 감소를 위해 사용됩니다.
 * @author snowykte0426
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${spring.cache.expire-after-write}")
    private long expireAfterWrite;
    private static final long MAXIMUM_CACHE_SIZE = 50;

    @Bean
    public CaffeineCacheManager cacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager("categories");
        caffeineCacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(expireAfterWrite, TimeUnit.MINUTES)
                        .maximumSize(MAXIMUM_CACHE_SIZE)
        );
        return caffeineCacheManager;
    }
}