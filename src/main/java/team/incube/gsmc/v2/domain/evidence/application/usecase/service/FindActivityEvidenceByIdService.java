package team.incube.gsmc.v2.domain.evidence.application.usecase.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.application.usecase.FindActivityEvidenceByEvidenceIdUseCase;
import team.incube.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incube.gsmc.v2.domain.evidence.exception.ActivityEvidenceNotFountException;
import team.incube.gsmc.v2.domain.evidence.presentation.data.response.GetActivityEvidenceResponse;
import team.incube.gsmc.v2.domain.member.domain.Member;
import team.incube.gsmc.v2.domain.member.domain.constant.MemberRole;
import team.incube.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;
/**
 * 활동 증빙자료 단일 조회 서비스 클래스입니다.
 * <p>
 * 주어진 활동 증빙자료 ID를 통해 해당 증빙자료를 조회하고,
 * 요청한 사용자가 해당 자료를 조회할 권한이 있는지 확인하는 로직을 포함합니다.
 * <p>{@link FindActivityEvidenceByEvidenceIdUseCase}를 구현하며,
 * 학생 권한의 경우 자신의 자료만 조회할 수 있도록 제한합니다.
 *
 * @author c0ng_yun
 */
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
            throw new ActivityEvidenceNotFountException();
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