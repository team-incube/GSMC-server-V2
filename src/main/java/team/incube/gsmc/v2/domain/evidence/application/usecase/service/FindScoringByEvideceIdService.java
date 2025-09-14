package team.incube.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.application.usecase.FindScoringByEvideceIdUseCase;
import team.incube.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incube.gsmc.v2.domain.evidence.exception.OtherEvidenceNotFoundException;
import team.incube.gsmc.v2.domain.evidence.presentation.data.response.GetOtherEvidenceResponse;
import team.incube.gsmc.v2.domain.member.domain.Member;
import team.incube.gsmc.v2.domain.member.domain.constant.MemberRole;
import team.incube.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;

/**
 * 채점 증빙자료를 단일 조회하는 서비스 클래스입니다.
 * <p>주어진 채점 증빙자료 ID를 통해 해당 증빙자료를 조회하고,
 * 요청한 사용자가 해당 자료를 조회할 권한이 있는지 확인하는 로직을 포함합니다.
 * <p>{@link FindScoringByEvideceIdUseCase}를 구현하며,
 * 학생 권한의 경우 자신의 자료만 조회할 수 있도록 제한합니다.
 * @author c0ng_yun
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindScoringByEvideceIdService implements FindScoringByEvideceIdUseCase {

    private final CurrentMemberProvider currentMemberProvider;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;

    @Override
    public GetOtherEvidenceResponse execute(Long id) {
        Member member = currentMemberProvider.getCurrentUser();
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
