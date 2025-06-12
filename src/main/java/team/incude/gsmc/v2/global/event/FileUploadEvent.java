package team.incude.gsmc.v2.global.event;

import team.incude.gsmc.v2.domain.evidence.domain.Evidence;

import java.io.InputStream;

public record FileUploadEvent(
        Long evidenceId,
        String fileName,
        InputStream inputStream
) {
}
