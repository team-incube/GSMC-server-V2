package team.incube.gsmc.v2.global.thirdparty.aws.data;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * AWS 환경 설정을 위한 데이터 클래스입니다.
 * <p>이 클래스는 AWS S3 버킷과 관련된 설정을 포함하며, Spring Boot의 {@link ConfigurationProperties}를 사용하여
 * application.yml 또는 application.properties 파일에서
 * 설정 값을 자동으로 매핑합니다.
 * <p>주요 필드로는 AWS 액세스 키 ID, 비밀 액세스 키, 그리고 S3 버킷의 이름과 지역이 있습니다.
 * * 이 설정을 통해 AWS S3와의 통신을 위한 인증 정보와 버킷 정보를 관리할 수 있습니다.
 * @author jihoonwjj
 * @param accessKeyId
 * @param secretAccessKey
 * @param bucket
 */
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