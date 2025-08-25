package team.incube.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.application.usecase.FindOtherByEvidenceIdUseCase;
import team.incube.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incube.gsmc.v2.domain.evidence.exception.OtherEvidenceNotFoundException;
import team.incube.gsmc.v2.domain.evidence.presentation.data.response.GetOtherEvidenceResponse;
import team.incube.gsmc.v2.domain.member.domain.Member;
import team.incube.gsmc.v2.domain.member.domain.constant.MemberRole;
import team.incube.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindOtherByEvidenceIdService implements FindOtherByEvidenceIdUseCase {
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final CurrentMemberProvider currentMemberProvider;
    @Override
    public GetOtherEvidenceResponse execute(Long id) {
        Member member =  currentMemberProvider.getCurrentUser();
        OtherEvidence evidence = otherEvidencePersistencePort.findOtherEvidenceById(id);
        if(member.getRole().equals(MemberRole.ROLE_STUDENT)&&
                !evidence.getId().getScore().getMember().equals(member)) {
            throw new OtherEvidenceNotFoundException();
        }
        return new GetOtherEvidenceResponse(
                evidence.getId().getId(),
                evidence.getFileUri(),
                evidence.getId().getEvidenceType(),
                evidence.getId().getReviewStatus(),
                evidence.getId().getScore().getCategory().getName()
        );
    }
}
