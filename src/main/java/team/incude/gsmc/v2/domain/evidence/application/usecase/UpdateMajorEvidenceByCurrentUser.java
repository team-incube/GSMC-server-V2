package team.incude.gsmc.v2.domain.evidence.application.usecase;

import org.springframework.web.multipart.MultipartFile;

public interface UpdateMajorEvidenceByCurrentUser {
    void execute(Long evidenceId, String title, String content, MultipartFile file);
}
