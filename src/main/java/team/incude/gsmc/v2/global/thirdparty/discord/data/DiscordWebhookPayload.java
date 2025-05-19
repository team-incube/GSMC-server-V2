
package team.incude.gsmc.v2.global.thirdparty.discord.data;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Discord 웹훅 전송 시 사용되는 최상위 페이로드 객체입니다.
 * <p>Discord의 웹훅 API는 Embed 형식의 메시지 전송을 지원하며,
 * 이 클래스는 Embed 리스트를 포함하는 JSON 구조를 나타냅니다.
 * <ul>
 *   <li>{@code embeds} - Discord에 표시할 임베드 메시지 리스트</li>
 * </ul>
 * 이 객체는 Jackson을 통해 직렬화되어 HTTP POST 요청의 본문으로 사용됩니다.
 * @author snowykte0426
 */
public record DiscordWebhookPayload(@JsonProperty("embeds") List<DiscordEmbed> embeds) {
}