package team.incube.gsmc.v2.global.util;

import lombok.experimental.UtilityClass;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * InputStream 관련 유틸리티 메서드를 제공하는 클래스입니다.
 * <p>{@link InputStream}은 기본적으로 한 번만 읽을 수 있기 때문에,
 * 동일한 데이터를 여러 번 사용하기 위해 복제 기능을 제공합니다.
 * <p>{@code @UtilityClass}로 선언되어 정적 메서드만 포함하며 인스턴스화할 수 없습니다.
 *
 * <p>주로 S3 업로드 실패 시 재시도 처리 등을 위해 {@link InputStream}을 임시로 복제하는 데 사용됩니다.
 *
 * @author suuuuuuminnnnnn
 */
@UtilityClass
public class InputStreamUtil {

    /**
     * 주어진 InputStream을 읽고 동일한 내용을 갖는 새로운 {@link ByteArrayInputStream}으로 반환합니다.
     * <p>원본 InputStream은 소모되며, 복제된 스트림은 이후 재사용이 가능합니다.
     *
     * @param inputStream 복제할 InputStream
     * @return 복제된 ByteArrayInputStream
     * @throws IOException InputStream을 읽는 도중 문제가 발생한 경우
     */
    public static ByteArrayInputStream duplicate(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }
            byte[] bytes = baos.toByteArray();
            return new ByteArrayInputStream(bytes);
        }
    }
}
