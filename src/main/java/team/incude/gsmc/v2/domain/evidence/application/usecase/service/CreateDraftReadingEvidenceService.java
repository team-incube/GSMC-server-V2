package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.evidence.application.port.DraftReadingEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.CreateDraftReadingEvidenceUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.DraftReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.CreateDraftEvidenceResponse;

import java.util.UUID;

/**
 * 독서 증빙자료 임시저장을 처리하는 유스케이스 구현 클래스입니다.
 * <p>{@link CreateDraftReadingEvidenceUseCase}를 구현하며, 책 제목, 저자, 페이지 수, 독서 내용을 기반으로
 * 독서 증빙자료를 임시저장합니다. TTL은 7일로 설정되어 자동 만료됩니다.
 * <p>기존 ID가 주어지면 해당 ID를 사용해 갱신하고, 없을 경우 새로운 UUID를 생성하여 저장합니다.

 * @author suuuuuuminnnnnn
 */
@Service
@RequiredArgsConstructor
public class CreateDraftReadingEvidenceService implements CreateDraftReadingEvidenceUseCase {

    private static final Long DRAFT_TTL_SECONDS = 7 * 24 * 60 * 60L; // 7일

    private final DraftReadingEvidencePersistencePort draftReadingEvidencePersistencePort;

    /**
     * 독서 증빙자료 임시저장을 생성하거나 갱신합니다.
     * @param id 기존 임시저장 ID (null이면 새로 생성)
     * @param title 책 제목
     * @param author 저자명
     * @param page 페이지 수
     * @param content 독서 내용 요약
     * @return 생성된 임시저장 ID 응답 객체
     */
    @Override
    public CreateDraftEvidenceResponse execute(UUID id, String title, String author, Integer page, String content) {
        UUID draftId = id == null ? UUID.randomUUID() : id;

        DraftReadingEvidence draftReadingEvidence = createReadingEvidence(draftId, title, page, author, content);

        draftReadingEvidencePersistencePort.saveDraftReadingEvidence(draftReadingEvidence);
        return new CreateDraftEvidenceResponse(draftId);
    }

    /**
     * DraftReadingEvidence 도메인 객체를 생성합니다.
     * @param id 임시저장 ID
     * @param title 책 제목
     * @param page 페이지 수
     * @param author 저자명
     * @param content 독서 내용 요약
     * @return 생성된 DraftReadingEvidence 객체
     */
    private DraftReadingEvidence createReadingEvidence(UUID id, String title, Integer page, String author, String content) {
        return DraftReadingEvidence.builder()
                .id(id)
                .title(title)
                .page(page)
                .author(author)
                .content(content)
                .ttl(DRAFT_TTL_SECONDS)
                .build();
    }
}