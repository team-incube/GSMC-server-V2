package team.incude.gsmc.v2.global.util;

import lombok.experimental.UtilityClass;
import team.incude.gsmc.v2.global.util.exception.InvalidFileUrlException;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * S3 파일 URI에서 파일 키를 추출하는 유틸리티 클래스입니다.
 * <p>파일 URI 전체에서 경로(Path) 부분을 추출하여 S3 키로 변환합니다.
 * 유효하지 않은 URL 형식의 입력이 들어올 경우 {@link InvalidFileUrlException}을 발생시킵니다.
 * <p>{@code @UtilityClass}로 선언되어 정적 메서드만 포함되며 인스턴스화되지 않습니다.
 * @author snowykte0426
 */
@UtilityClass
public class ExtractFileKeyUtil {

    public String extractFileKey(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            return url.getPath().substring(1);
        } catch (MalformedURLException e) {
            throw new InvalidFileUrlException();
        }
    }
}