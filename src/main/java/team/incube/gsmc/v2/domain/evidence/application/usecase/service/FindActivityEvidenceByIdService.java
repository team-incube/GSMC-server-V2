package team.incube.gsmc.v2.domain.evidence.application.usecase.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.application.usecase.FindActivityEvidenceByEvidenceIdUseCase;
import team.incube.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incube.gsmc.v2.domain.evidence.exception.ActivityEvidenceAccessDeniedException;
import team.incube.gsmc.v2.domain.evidence.presentation.data.response.GetActivityEvidenceResponse;
import team.incube.gsmc.v2.domain.member.domain.Member;
import team.incube.gsmc.v2.domain.member.domain.constant.MemberRole;
import team.incube.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindActivityEvidenceByIdService implements FindActivityEvidenceByEvidenceIdUseCase {

    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;
    private final CurrentMemberProvider currentMemberProvider;

    @Override
    public GetActivityEvidenceResponse execute(Long id) {
        Member member = currentMemberProvider.getCurrentUser();
        ActivityEvidence evidence = activityEvidencePersistencePort.findActivityEvidenceById(id);
        if(member.getRole().equals(MemberRole.ROLE_STUDENT) && !evidence.getId().getScore().getMember().equals(member)) {
            throw new ActivityEvidenceAccessDeniedException();
        }
        return new GetActivityEvidenceResponse(
                evidence.getId().getId(),
                evidence.getTitle(),
                evidence.getContent(),
                evidence.getImageUrl(),
                evidence.getId().getReviewStatus(),
                evidence.getId().getScore().getCategory().getName()
        );

    }
}