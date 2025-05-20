package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.evidence.application.port.DraftActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.DraftReadingEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.FindDraftEvidenceByCurrentUserUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.DraftActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.DraftReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetDraftActivityEvidenceResponse;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetDraftEvidenceResponse;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetDraftReadingEvidenceResponse;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.CurrentMemberProvider;

import java.util.List;

/**
 * 현재 사용자 기준으로 모든 임시저장 증빙자료를 조회하는 유스케이스 구현 클래스입니다.
 * <p>{@link FindDraftEvidenceByCurrentUserUseCase}를 구현하며,
 * 활동 및 독서 증빙자료의 임시저장을 각각 조회한 뒤 통합 응답으로 반환합니다.
 * <p>임시저장은 사용자의 이메일 기준으로 조회되며, 각각의 응답 객체 리스트로 변환됩니다.
 * @author suuuuuuminnnnnn
 */
@Service
@RequiredArgsConstructor
public class FindDraftEvidenceByCurrentUserService implements FindDraftEvidenceByCurrentUserUseCase {

    private final DraftActivityEvidencePersistencePort draftActivityEvidencePersistencePort;
    private final DraftReadingEvidencePersistencePort draftReadingEvidencePersistencePort;
    private final CurrentMemberProvider currentMemberProvider;

    /**
     * 현재 로그인한 사용자의 활동 및 독서 임시저장 증빙자료를 조회합니다.
     * @return 활동/독서 임시저장을 포함한 통합 응답 DTO
     */
    @Override
    public GetDraftEvidenceResponse execute() {
        Member member = currentMemberProvider.getCurrentUser();

        List<DraftActivityEvidence> activityEvidences = draftActivityEvidencePersistencePort.findAllDraftActivityEvidenceByEmail(member.getEmail());
        List<DraftReadingEvidence> readingEvidences = draftReadingEvidencePersistencePort.findAllDraftReadingEvidenceByEmail(member.getEmail());

        List<GetDraftActivityEvidenceResponse> activityEvidenceResponses = createDraftActivityResponse(activityEvidences);
        List<GetDraftReadingEvidenceResponse> readingEvidenceResponses = createDraftReadingResponse(readingEvidences);

        return new GetDraftEvidenceResponse(activityEvidenceResponses, readingEvidenceResponses);
    }

    /**
     * 활동 임시저장 엔티티 리스트를 응답 DTO 리스트로 변환합니다.
     * @param activityEvidences 활동 임시저장 리스트
     * @return 변환된 활동 임시저장 응답 리스트
     */
    private List<GetDraftActivityEvidenceResponse> createDraftActivityResponse(List<DraftActivityEvidence> activityEvidences) {
        return activityEvidences.stream()
                .map(e -> new GetDraftActivityEvidenceResponse(
                        e.getId(),
                        e.getTitle(),
                        e.getContent(),
                        e.getImageUrl(),
                        e.getCategoryName(),
                        e.getEvidenceType()
                ))
                .toList();
    }

    /**
     * 독서 임시저장 엔티티 리스트를 응답 DTO 리스트로 변환합니다.
     * @param readingEvidences 독서 임시저장 리스트
     * @return 변환된 독서 임시저장 응답 리스트
     */
    private List<GetDraftReadingEvidenceResponse> createDraftReadingResponse(List<DraftReadingEvidence> readingEvidences) {
        return readingEvidences.stream()
                .map(e -> new GetDraftReadingEvidenceResponse(
                        e.getId(),
                        e.getTitle(),
                        e.getAuthor(),
                        e.getPage(),
                        e.getContent()
                ))
                .toList();
    }
}