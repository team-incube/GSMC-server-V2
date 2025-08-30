package team.incube.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.application.usecase.FindReadingByEvidenceIdUseCase;
import team.incube.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incube.gsmc.v2.domain.evidence.exception.ReadingEvidenceNotFoundException;
import team.incube.gsmc.v2.domain.evidence.presentation.data.response.GetReadingEvidenceResponse;
import team.incube.gsmc.v2.domain.member.domain.Member;
import team.incube.gsmc.v2.domain.member.domain.constant.MemberRole;
import team.incube.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;
/**
 * 독서 증빙자료를 단일 조회하는 서비스 클래스입니다.
 * <p>
 * 주어진 독서 증빙자료 ID를 통해 해당 증빙자료를 조회하고,
 * 요청한 사용자가 해당 자료를 조회할 권한이 있는지 확인하는 로직을 포함합니다.
 * <p>{@link FindReadingByEvidenceIdUseCase}를 구현하며,
 * 학생 권한의 경우 자신의 자료만 조회할 수 있도록 제한합니다.
 *
 * @author c0ng_yun
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindReadingByEvidenceIdService implements FindReadingByEvidenceIdUseCase {

    private final ReadingEvidencePersistencePort readingEvidencePersistencePort;
    private final CurrentMemberProvider currentMemberProvider;

    @Override
    public GetReadingEvidenceResponse execute(Long id) {
        Member member = currentMemberProvider.getCurrentUser();
        ReadingEvidence evidence = readingEvidencePersistencePort.findReadingEvidenceById(id);
        if(member.getRole().equals(MemberRole.ROLE_STUDENT)
                &&!evidence.getId().getScore().getMember().equals(member)) {
            throw new ReadingEvidenceNotFoundException();
        }
        return new GetReadingEvidenceResponse(
                evidence.getId().getId(),
                evidence.getTitle(),
                evidence.getAuthor(),
                evidence.getPage(),
                evidence.getContent(),
                evidence.getId().getReviewStatus()
        );
    }
}
