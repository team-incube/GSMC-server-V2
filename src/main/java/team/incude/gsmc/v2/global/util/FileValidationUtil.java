package team.incude.gsmc.v2.global.util;

import lombok.experimental.UtilityClass;
import team.incude.gsmc.v2.global.util.exception.EmptyFileException;
import team.incude.gsmc.v2.global.util.exception.FileNameTooLongException;

import java.io.IOException;
import java.io.InputStream;

@UtilityClass
public class FileValidationUtil {

    private final int MAX_FILE_URL_LENGTH = 255;

    private void validateFileName(String bucketName, String region, String fileName) {
        if (String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName).length() > MAX_FILE_URL_LENGTH) {
            throw new FileNameTooLongException();
        }
    }

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