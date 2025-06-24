package team.incube.gsmc.v2.global.thirdparty.discord.usecase.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team.incube.gsmc.v2.global.thirdparty.discord.EmbedColor;
import team.incube.gsmc.v2.global.thirdparty.discord.data.DiscordEmbed;
import team.incube.gsmc.v2.global.thirdparty.discord.data.DiscordWebhookPayload;

import java.time.Instant;
import java.util.List;

/**
 * Spring Boot 애플리케이션의 생명주기 이벤트(시작, 종료)를 Discord 웹훅으로 전송하는 서비스입니다.
 * <p>{@link SmartLifecycle}을 구현하여 서버 시작 시점과 종료 시점에 Discord로 알림 메시지를 전송합니다.
 * <p>운영 환경(`!dev`, `!test` 프로필)에서만 활성화됩니다.
 * <ul>
 *   <li>🚀 서버 시작됨: Spring Boot 애플리케이션 시작 시 호출</li>
 *   <li>🛑 서버 종료됨: 애플리케이션 종료 시 호출</li>
 * </ul>
 * Discord 메시지는 임베드 형식으로 전송되며, 색상 및 타임스탬프를 포함합니다.
 * @author snowykte0426
 */
@Profile("!dev & !test")
@Slf4j
@Service
public class DiscordApplicationLifecycleNotifyService implements SmartLifecycle {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${webhook.discord.url}")
    private String webhookUrl;

    private boolean running = false;

    /**
     * 애플리케이션이 시작될 때 호출되며, Discord에 서버 시작 메시지를 전송합니다.
     */
    @Override
    public void start() {
        sendEmbed("🚀 서버 시작됨", "Spring Boot 애플리케이션이 시작되었습니다.", EmbedColor.SERVER_START.getColor());
        running = true;
    }

    /**
     * 애플리케이션이 종료될 때 호출되며, Discord에 서버 종료 메시지를 전송합니다.
     */
    @Override
    public void stop() {
        sendEmbed("🛑 서버 종료됨", "Spring Boot 애플리케이션이 종료되었습니다.", EmbedColor.SERVER_STOP.getColor());
        running = false;
    }

    /**
     * 현재 애플리케이션이 실행 중인지 여부를 반환합니다.
     * @return 실행 중이면 true, 아니면 false
     */
    @Override
    public boolean isRunning() {
        return running;
    }

    /**
     * 지정된 제목과 설명, 색상으로 Discord 웹훅 메시지를 전송합니다.
     * @param title       임베드 제목
     * @param description 임베드 설명
     * @param color       Discord 임베드 색상 코드
     */
    private void sendEmbed(String title, String description, int color) {
        DiscordEmbed embed = new DiscordEmbed(title, color, null, Instant.now().toString());
        DiscordWebhookPayload payload = new DiscordWebhookPayload(List.of(embed));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DiscordWebhookPayload> request = new HttpEntity<>(payload, headers);

        try {
            restTemplate.postForEntity(webhookUrl, request, Void.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}