package team.incude.gsmc.v2.global.thirdparty.discord.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team.incude.gsmc.v2.global.thirdparty.discord.service.SendEvidenceUploadFailureAlertUseCase;

import java.util.Map;

@Slf4j
@Service
public class SendEvidenceUploadFailureAlertService implements SendEvidenceUploadFailureAlertUseCase {

    @Value("${webhook.discord.url}")
    private String webhookUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public SendEvidenceUploadFailureAlertService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

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
