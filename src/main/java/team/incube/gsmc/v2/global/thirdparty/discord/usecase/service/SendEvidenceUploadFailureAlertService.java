package team.incube.gsmc.v2.global.thirdparty.discord.usecase.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team.incube.gsmc.v2.global.thirdparty.discord.usecase.SendEvidenceUploadFailureAlertUseCase;

import java.util.Map;

/**
 * 증빙자료 업로드 실패 시 Discord 웹훅을 통해 알림을 전송하는 유스케이스 구현 클래스입니다.
 * <p>{@link SendEvidenceUploadFailureAlertUseCase}를 구현하며, S3 파일 업로드 실패 시
 * 관리자에게 즉시 알림을 제공하여 빠른 대응이 가능하도록 합니다.
 * <p>Discord 웹훅 URL은 {@code webhook.discord.url} 프로퍼티를 통해 설정되며,
 * {@link RestTemplate}을 사용하여 HTTP POST 요청으로 메시지를 전송합니다.
 * <p>알림 전송 실패 시에도 시스템 전체에 영향을 주지 않도록 예외를 캐치하여 로그로만 기록합니다.
 * @author suuuuuuminnnnnn
 */
@Slf4j
@Service
public class SendEvidenceUploadFailureAlertService implements SendEvidenceUploadFailureAlertUseCase {

    @Value("${webhook.discord.url}")
    private String webhookUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Discord 알림 전송을 위한 RestTemplate과 ObjectMapper를 초기화합니다.
     */
    public SendEvidenceUploadFailureAlertService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 증빙자료 업로드 실패 정보를 Discord 채널로 전송합니다.
     * <p>사용자 이메일, 증빙 ID, 파일명, 예외 메시지를 포함한 알림 메시지를 생성하여
     * Discord 웹훅을 통해 전송합니다.
     * <p>메시지는 마크다운 형식으로 포맷되어 가독성을 높이며,
     * 전송 실패 시에도 시스템에 영향을 주지 않도록 예외를 처리합니다.
     * @param evidenceId 업로드 실패한 증빙자료의 ID
     * @param fileName 업로드 실패한 파일명
     * @param email 업로드를 시도한 사용자의 이메일
     * @param exception 발생한 예외 객체
     */
    @Override
    public void sendEvidenceUploadFailureAlert(Long evidenceId, String fileName, String email, Throwable exception) {
        String content = String.format("""
                🚨 **증빙자료 업로드 실패**
                - 사용자: %s
                - 증빙 ID: %d
                - 파일명: `%s`
                - 예외: `%s`
                """, email, evidenceId, fileName, exception.getMessage());
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String body = objectMapper.writeValueAsString(Map.of("content", content));
            HttpEntity<String> request = new HttpEntity<>(body, headers);

            restTemplate.postForEntity(webhookUrl, request, Void.class);
        } catch (Exception e) {
            log.error("[Discord Alert 실패] {}", e.getMessage(), e);
        }
    }
}
