package team.incube.gsmc.v2.global.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import team.incube.gsmc.v2.global.security.jwt.data.JwtEnvironment;
import team.incube.gsmc.v2.global.thirdparty.aws.data.AwsEnvironment;

@EnableConfigurationProperties({AwsEnvironment.class, JwtEnvironment.class})
@Configuration
public class PropertiesScanConfig {
}
