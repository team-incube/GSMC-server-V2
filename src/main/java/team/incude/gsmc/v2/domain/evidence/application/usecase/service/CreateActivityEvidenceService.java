package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.application.usecase.CreateActivityEvidenceUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.CurrentMemberProvider;

@Service
@RequiredArgsConstructor
public class CreateActivityEvidenceService implements CreateActivityEvidenceUseCase {

    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;
    private final ScorePersistencePort scorePersistencePort;
    private final S3Port s3Port;
    private final CurrentMemberProvider currentMemberProvider;


    @Override
    public void execute(String categoryName, String title, String content, MultipartFile file, EvidenceType activityType) {
        Member member = currentMemberProvider.getCurrentUser();
    }
}
