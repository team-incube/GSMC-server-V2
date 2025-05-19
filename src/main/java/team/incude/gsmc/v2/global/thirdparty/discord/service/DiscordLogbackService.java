package team.incude.gsmc.v2.global.thirdparty.discord.service;

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

    private String truncate(String str) {
        return str.length() > 800 ? str.substring(0, 900) + "..." : str;
    }
}