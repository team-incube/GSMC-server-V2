package team.incude.gsmc.v2.global.thirdparty.discord;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EmbedColor {
    SERVER_START(0x3498DB),
    SERVER_STOP(0x2ECC71),
    ERROR(0xE74C3C);

    private final int color;
}