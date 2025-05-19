package team.incude.gsmc.v2.global.thirdparty.discord.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Discord 웹훅 메시지의 Embed 객체를 표현하는 레코드 클래스입니다.
 * <p>Embed는 Discord 메시지에서 시각적으로 강조된 콘텐츠 블록이며,
 * 제목, 색상, 필드 목록, 타임스탬프를 포함합니다.
 * <ul>
 *   <li>{@code title} - 임베드 제목</li>
 *   <li>{@code color} - 좌측 색상 바 (정수형 RGB)</li>
 *   <li>{@code fields} - 상세 필드 항목 리스트</li>
 *   <li>{@code timestamp} - 메시지 생성 시간 (ISO-8601 문자열)</li>
 * </ul>
 * 이 클래스는 Discord에 JSON 직렬화되어 전송됩니다.
 * @author snowykte0426
 */
public record DiscordEmbed(
        @JsonProperty("title") String title,
        @JsonProperty("color") int color,
        @JsonProperty("fields") List<DiscordField> fields,
        @JsonProperty("timestamp") String timestamp
) {}