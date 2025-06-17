package team.incude.gsmc.v2.domain.evidence.application.usecase;

import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;

import java.io.InputStream;

public interface UpdateEvidenceFileUseCase {
    void execute(Long evidenceId, String fileName, InputStream inputStream, EvidenceType evidenceType);
}