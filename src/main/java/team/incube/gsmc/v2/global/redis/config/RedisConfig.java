package team.incube.gsmc.v2.global.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

/**
 * Redis 관련 설정을 정의하는 구성 클래스입니다.
 * <p>Lettuce 기반 RedisConnectionFactory를 생성하고,
 * 문자열 기반 직렬화를 사용하는 {@link RedisTemplate}을 빈으로 등록합니다.</p>
 * <p>이 설정을 통해 Redis에 문자열 키-값 데이터를 저장하거나 조회할 수 있습니다.</p>
 * <p>host와 port는 application.yml 또는 application.properties의
 * {@code spring.data.redis.host}, {@code spring.data.redis.port}에서 주입됩니다.</p>
 * @author suuuuuuminnnnnn, sunowykte0426
 */
@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password:}")
    private String password;

    /**
     * RedisConnectionFactory를 생성합니다.
     * <p>RedisStandaloneConfiguration을 사용하여 호스트, 포트, 비밀번호를 설정합니다.</p>
     * @return LettuceConnectionFactory
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        if (StringUtils.hasText(password)) {
            config.setPassword(password);
        }
        return new LettuceConnectionFactory(config);
    }

    /**
     * RedisTemplate을 생성합니다.
     * <p>RedisConnectionFactory를 사용하여 RedisTemplate을 설정하고,
     * 문자열 직렬화를 사용하여 키와 값을 직렬화합니다.</p>
     * @return RedisTemplate<String, String>
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
