package team.incude.gsmc.v2.global.thirdparty.discord.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Discord 웹훅 Embed 메시지 내에 포함되는 단일 필드 정보를 나타내는 레코드입니다.
 * <p>Embed 안의 필드 블록은 제목(name), 내용(value), 줄바꿈 여부(inline)로 구성됩니다.
 * Discord 메시지에 추가적인 구조화된 정보를 제공하기 위해 사용됩니다.
 * <ul>
 *   <li>{@code name} - 필드의 제목</li>
 *   <li>{@code value} - 필드의 내용</li>
 *   <li>{@code inline} - true일 경우 다른 필드와 한 줄에 나열됨</li>
 * </ul>
 * 이 객체는 {@link DiscordEmbed}에 포함되어 전송됩니다.
 * @author snowykte0426
 */
public record DiscordField(
        @JsonProperty("name") String name,
        @JsonProperty("value") String value,
        @JsonProperty("inline") boolean inline
) {}