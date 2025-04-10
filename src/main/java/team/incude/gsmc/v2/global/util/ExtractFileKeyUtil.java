package team.incude.gsmc.v2.global.util;

import lombok.experimental.UtilityClass;
import team.incude.gsmc.v2.global.util.exception.InvalidFileUrlException;

import java.net.MalformedURLException;
import java.net.URL;

@UtilityClass
public class ExtractFileKeyUtil {

    public String extractFileKey(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            String path = url.getPath();
            return path.substring(path.lastIndexOf("/") + 1);
        } catch (MalformedURLException e) {
            throw new InvalidFileUrlException();
        }
    }
}