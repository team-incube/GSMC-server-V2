package team.incude.gsmc.v2.global.util;

import lombok.experimental.UtilityClass;

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