package team.incude.gsmc.v2.global.thirdparty.aws.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import team.incude.gsmc.v2.global.thirdparty.aws.data.AwsEnvironment;

@EnableConfigurationProperties(AwsEnvironment.class)
@Configuration
public class PropertiesScanConfig {
}
