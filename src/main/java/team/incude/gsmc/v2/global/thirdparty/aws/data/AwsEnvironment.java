package team.incude.gsmc.v2.global.thirdparty.aws.data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="aws")
public record AwsEnvironment(

    String accessKeyId,
    String secretAccessKey,
    BucketProperties bucket
) {
    public record BucketProperties(
            String name,
            String region
    ){}
}
