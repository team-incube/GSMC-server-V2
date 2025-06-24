package team.incube.gsmc.v2.global.util;

import lombok.experimental.UtilityClass;

/**
 * snake_case 또는 kebab-case 문자열을 camelCase 형식으로 변환하는 유틸리티 클래스입니다.
 * <p>주로 JSON 키, 프로퍼티 이름 등에서 스네이크/케밥 케이스로 작성된 문자열을 Java 객체의 필드 명명 규칙인 camelCase로 변환할 때 사용됩니다.
 * <p>{@code @UtilityClass}로 선언되어 정적 메서드만 포함하며 인스턴스화되지 않습니다.
 * <p>사용 예시:
 * <pre>
 *   toCamelCase("example_string-key") // "exampleStringKey"
 * </pre>
 * @author snowykte0426
 */
@UtilityClass
public class SnakeKebabToCamelCaseConverterUtil {
    public String toCamelCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        boolean upperNext = false;
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (ch == '_' || ch == '-') {
                upperNext = true;
            } else {
                if (result.isEmpty()) {
                    result.append(Character.toLowerCase(ch));
                } else if (upperNext) {
                    result.append(Character.toUpperCase(ch));
                    upperNext = false;
                } else {
                    result.append(Character.toLowerCase(ch));
                }
            }
        }
        return result.toString();
    }
}