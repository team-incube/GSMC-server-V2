package team.incude.gsmc.v2.global.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValueLimiterUtil {
    public boolean isExceedingLimit(int value, int maxLimit) {
        return value > maxLimit;
    }
}