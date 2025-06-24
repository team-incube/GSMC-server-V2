package team.incude.gsmc.v2.global.thirdparty.aws.s3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import team.incude.gsmc.v2.global.thirdparty.aws.data.AwsEnvironment;


/**
 * AWS S3 연동을 위한 비동기 클라이언트 설정 클래스입니다.
 * <p>{@link S3AsyncClient}를 Spring Bean으로 등록하여 S3 파일 업로드 및 삭제 기능에서 사용할 수 있도록 구성합니다.
 * <p>액세스 키, 시크릿 키, 리전 정보는 {@code application.yml} 또는 환경 변수로부터 주입됩니다.
 * <p>이 구성은 AWS SDK v2 기반의 비동기 S3 클라이언트를 사용하여 비동기 I/O 성능을 극대화합니다.
 * @author snowykte0426
 */
@Configuration
public class S3Config {

    private AwsEnvironment awsEnvironment;

    @Bean
    public S3AsyncClient s3AsyncClient() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(awsEnvironment.accessKeyId(), awsEnvironment.secretAccessKey());
        return S3AsyncClient.builder()
                .region(Region.of(awsEnvironment.bucket().region()))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }
}