package team.incube.gsmc.v2.global.thirdparty.discord;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Discord 알림 메시지에서 사용되는 색상 값을 정의하는 열거형입니다.
 * <p>각 색상은 16진수 RGB 코드로 정의되며, 서버 시작, 종료, 오류 등의 이벤트 상황에 따라 다른 색상이 사용됩니다.
 * <ul>
 *   <li>{@code SERVER_START}: 파란색 (정보)</li>
 *   <li>{@code SERVER_STOP}: 녹색 (정상 종료)</li>
 *   <li>{@code ERROR}: 빨간색 (오류)</li>
 * </ul>
 * Discord Embed 메시지의 시각적 강조를 위해 사용됩니다.
 * @author snowykte0426
 */
@RequiredArgsConstructor
@Getter
public enum EmbedColor {
    SERVER_START(0x3498DB),
    SERVER_STOP(0x2ECC71),
    ERROR(0xE74C3C);

    private final int color;
}