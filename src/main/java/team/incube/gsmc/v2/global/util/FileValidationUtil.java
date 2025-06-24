package team.incube.gsmc.v2.global.util;

import lombok.experimental.UtilityClass;
import team.incube.gsmc.v2.global.util.exception.EmptyFileException;
import team.incube.gsmc.v2.global.util.exception.FileNameTooLongException;

import java.io.IOException;
import java.io.InputStream;

/**
 * 파일 유효성 검사를 위한 유틸리티 클래스입니다.
 * <p>S3에 업로드되는 파일의 스트림 유효성 및 URL 길이 제한 등을 검증합니다.
 * 빈 파일 업로드나 과도하게 긴 파일 경로로 인한 오류를 방지하기 위해 사용됩니다.
 * <p>{@code @UtilityClass}로 선언되어 정적 유틸 메서드만 포함하며 인스턴스화되지 않습니다.
 * @author snowykte0426
 */
@UtilityClass
public class FileValidationUtil {

    private final int MAX_FILE_URL_LENGTH = 255;

    /**
     * S3 업로드 대상 파일의 URL 길이를 검증합니다.
     * <p>버킷 이름, 리전, 파일 이름으로 구성된 전체 URL이 최대 길이를 초과할 경우 예외를 발생시킵니다.
     * @param bucketName S3 버킷 이름
     * @param region 리전 정보
     * @param fileName 파일 이름
     * @throws FileNameTooLongException URL 길이가 허용 범위를 초과할 경우
     */
    private void validateFileName(String bucketName, String region, String fileName) {
        if (String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName).length() > MAX_FILE_URL_LENGTH) {
            throw new FileNameTooLongException();
        }
    }

    /**
     * 파일 스트림 및 파일 이름 유효성을 검증합니다.
     * <p>빈 파일인지 확인한 후, 파일 이름의 S3 URL 길이 제한을 검사합니다.
     * @param inputStream 파일 입력 스트림
     * @param bucketName S3 버킷 이름
     * @param region 리전 정보
     * @param fileName 파일 이름
     * @throws EmptyFileException 파일이 비어 있거나 스트림 처리 중 오류가 발생한 경우
     * @throws FileNameTooLongException 파일 이름으로 구성된 URL 길이가 허용 범위를 초과한 경우
     */
    public void validateFile(InputStream inputStream, String bucketName, String region, String fileName) {
        try {
            if (inputStream.available() == 0) {
                throw new EmptyFileException();
            }
        } catch (IOException e) {
            throw new EmptyFileException();
        }
        validateFileName(bucketName, region, fileName);
    }
}