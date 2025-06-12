package team.incude.gsmc.v2.global.event;

import java.io.InputStream;

public record FileUploadEvent(
        Long evidenceId,
        String fileName,
        InputStream inputStream
) {
}
