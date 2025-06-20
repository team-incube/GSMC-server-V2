package team.incude.gsmc.v2.global.thirdparty.discord.usecase.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import team.incude.gsmc.v2.global.thirdparty.discord.EmbedColor;
import team.incude.gsmc.v2.global.thirdparty.discord.data.DiscordEmbed;
import team.incude.gsmc.v2.global.thirdparty.discord.data.DiscordField;
import team.incude.gsmc.v2.global.thirdparty.discord.data.DiscordWebhookPayload;

import java.time.Instant;
import java.util.List;
import java.util.Objects;


/**
 * Logback의 ERROR 로그를 Discord 웹훅을 통해 전송하는 커스텀 Appender 클래스입니다.
 * <p>{@link AppenderBase}를 상속하여 Spring Boot 애플리케이션에서 발생하는 예외 로그를
 * Discord 채널로 실시간 알림 형태로 전송합니다.
 * <p>생성된 메시지는 임베드 형태로 전송되며, 예외 이름, 발생 위치, 메시지, 스택 트레이스 일부를 포함합니다.
 * <p>운영 환경에서만 활성화되며, {@code !dev & !test} 프로필 조건으로 제한됩니다.
 * @author snowykte0426
 */
@Profile("!dev & !test")
@Slf4j
public class DiscordLogbackService extends AppenderBase<ILoggingEvent> {

    @Setter
    private String webhookUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public DiscordLogbackService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * ERROR 수준 이상의 로그 이벤트가 발생했을 때 Discord 웹훅으로 로그 정보를 전송합니다.
     * <p>예외 정보가 포함된 경우, 예외 클래스명, 발생 위치, 메시지, 스택 트레이스 일부를 포함한 임베드 메시지를 구성합니다.
     * @param event 로그 이벤트 객체
     */
    @Override
    protected void append(ILoggingEvent event) {
        if (!event.getLevel().isGreaterOrEqual(Level.ERROR)) return;

        try {
            String message = event.getFormattedMessage();
            String exceptionName = "";
            String location = "";
            String stackTrace = "";

            if (event.getThrowableProxy() != null) {
                var proxy = event.getThrowableProxy();
                exceptionName = proxy.getClassName();

                var traces = proxy.getStackTraceElementProxyArray();
                if (traces != null && traces.length > 0) {
                    location = traces[0].getSTEAsString();
                }

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < Math.min(10, Objects.requireNonNull(traces).length); i++) {
                    sb.append(traces[i].getSTEAsString()).append("\n");
                }
                stackTrace = sb.toString();
            }

            DiscordEmbed embed = new DiscordEmbed(
                    "🚨 ERROR 로그 발생",
                    EmbedColor.ERROR.getColor(),
                    List.of(
                            new DiscordField("Exception", exceptionName.isEmpty() ? "N/A" : exceptionName, false),
                            new DiscordField("Location", location.isEmpty() ? "N/A" : "```" + location + "```", false),
                            new DiscordField("Message", "```" + truncate(message) + "```", false),
                            new DiscordField("StackTrace", "```" + truncate(stackTrace) + "```", false)
                    ),
                    Instant.ofEpochMilli(event.getTimeStamp()).toString()
            );

            DiscordWebhookPayload payload = new DiscordWebhookPayload(List.of(embed));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payload), headers);
            restTemplate.postForEntity(webhookUrl, request, Void.class);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Discord 웹훅 제한을 고려해 메시지를 최대 길이(950자)로 잘라냅니다.
     * @param str 자를 문자열
     * @return 자른 문자열
     */
    private String truncate(String str) {
        return str.length() > 950 ? str.substring(0, 950) + "..." : str;
    }
}