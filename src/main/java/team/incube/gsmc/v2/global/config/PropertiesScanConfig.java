package team.incube.gsmc.v2.global.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import team.incube.gsmc.v2.global.security.jwt.data.JwtEnvironment;
import team.incube.gsmc.v2.global.thirdparty.aws.data.AwsEnvironment;

/**
 * 애플리케이션 전역에서 사용할 프로퍼티 설정 클래스입니다.
 * <p>{@link EnableConfigurationProperties}를 통해 {@link AwsEnvironment}와 {@link JwtEnvironment} 클래스를
 * 프로퍼티 소스로 등록하여, 외부 설정 파일(application.yml 등)에서 주입받은 값을 사용할 수 있도록 합니다.
 * 이 설정은 AWS 관련 설정과 JWT 관련 설정을 관리하는 데 사용됩니다.
 * @author jihoonwjj
 */
@EnableConfigurationProperties({AwsEnvironment.class, JwtEnvironment.class})
@Configuration
public class PropertiesScanConfig {
}