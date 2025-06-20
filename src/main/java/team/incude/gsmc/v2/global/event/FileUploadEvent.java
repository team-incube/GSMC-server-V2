package team.incude.gsmc.v2.global.event;

import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;

import java.io.InputStream;

public record FileUploadEvent(
        Long evidenceId,
        String fileName,
        InputStream inputStream,
        EvidenceType evidenceType
) {
}
