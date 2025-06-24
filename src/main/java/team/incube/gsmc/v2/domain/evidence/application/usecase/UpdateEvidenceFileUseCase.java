package team.incube.gsmc.v2.domain.evidence.application.usecase;

import team.incube.gsmc.v2.domain.evidence.domain.constant.EvidenceType;

import java.io.InputStream;

public interface UpdateEvidenceFileUseCase {
    void execute(Long evidenceId, String fileName, InputStream inputStream, EvidenceType evidenceType, String email);
}