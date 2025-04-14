package team.incude.gsmc.v2.domain.evidence.application.usecase;

import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;

public interface UpdateActivityEvidenceByCurrentUserUseCase {
    void execute(Long evidenceId, String title, String content, MultipartFile file, EvidenceType evidenceType);
}
