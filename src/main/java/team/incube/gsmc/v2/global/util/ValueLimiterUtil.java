package team.incube.gsmc.v2.global.util;

import lombok.experimental.UtilityClass;

/**
 * 값이 제한 범위를 초과하는지 여부를 검사하는 유틸리티 클래스입니다.
 * <p>주로 점수 입력 시 최대 허용 값을 초과하는지 확인하는 데 사용되며,
 * 불필요한 조건문 중복을 줄이기 위한 단순 비교 유틸 메서드를 제공합니다.
 * <p>{@code @UtilityClass}로 선언되어 정적 메서드만 포함되며 인스턴스화되지 않습니다.
 * @author snowykte0426
 */
@UtilityClass
public class ValueLimiterUtil {
    public boolean isExceedingLimit(int value, int maxLimit) {
        return value > maxLimit;
    }
}