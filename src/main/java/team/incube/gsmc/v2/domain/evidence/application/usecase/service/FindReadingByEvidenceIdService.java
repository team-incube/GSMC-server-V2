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
