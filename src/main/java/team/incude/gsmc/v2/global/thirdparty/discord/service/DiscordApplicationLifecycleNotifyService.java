package team.incude.gsmc.v2.global.thirdparty.discord.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team.incude.gsmc.v2.global.thirdparty.discord.EmbedColor;
import team.incude.gsmc.v2.global.thirdparty.discord.data.DiscordEmbed;
import team.incude.gsmc.v2.global.thirdparty.discord.data.DiscordWebhookPayload;

import java.time.Instant;
import java.util.List;

@Profile("!dev & !test")
@Slf4j
@Service
public class DiscordApplicationLifecycleNotifyService implements SmartLifecycle {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${webhook.discord.url}")
    private String webhookUrl;

    private boolean running = false;

    @Override
    public void start() {
        sendEmbed("🚀 서버 시작됨", "Spring Boot 애플리케이션이 시작되었습니다.", EmbedColor.SERVER_START.getColor());
        running = true;
    }

    @Override
    public void stop() {
        sendEmbed("🛑 서버 종료됨", "Spring Boot 애플리케이션이 종료되었습니다.", EmbedColor.SERVER_STOP.getColor());
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

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