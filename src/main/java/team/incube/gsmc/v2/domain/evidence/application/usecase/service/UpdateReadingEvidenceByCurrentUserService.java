package team.incube.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gsmc.v2.domain.evidence.application.port.EvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.application.usecase.UpdateReadingEvidenceByCurrentUserUseCase;
import team.incube.gsmc.v2.domain.evidence.domain.Evidence;
import team.incube.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incube.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;

import java.time.LocalDateTime;

/**
 * 독서 증빙자료 수정을 처리하는 유스케이스 구현 클래스입니다.
 * <p>{@link UpdateReadingEvidenceByCurrentUserUseCase}를 구현하며,
 * 제목, 저자, 페이지 수, 독서 내용을 수정하고 검토 상태를 초기화하여 저장합니다.
 * <p>수정된 내용은 새로운 {@link Evidence} 및 {@link ReadingEvidence} 객체로 반영되며,
 * 검토 상태는 항상 {@code PENDING}으로 설정됩니다.
 * @author suuuuuuminnnnnn
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UpdateReadingEvidenceByCurrentUserService implements UpdateReadingEvidenceByCurrentUserUseCase {

    private final ReadingEvidencePersistencePort readingEvidencePersistencePort;
    private final EvidencePersistencePort evidencePersistencePort;

    /**
     * 독서 증빙자료를 수정합니다.
     * <p>기존 Evidence를 조회한 후, 제목, 저자, 페이지 수, 내용을 수정한 새로운 {@link ReadingEvidence} 객체로 대체합니다.
     * 검토 상태는 항상 {@code PENDING}으로 초기화되며, 수정된 정보는 저장소에 반영됩니다.
     * @param evidenceId 수정할 증빙자료의 ID
     * @param title 새로운 책 제목
     * @param author 새로운 저자명
     * @param content 새로운 독서 내용 요약
     * @param page 새로운 페이지 수
     */
    @Override
    public void execute(Long evidenceId, String title, String author, String content, int page) {
        Evidence evidence = evidencePersistencePort.findEvidenceByIdWithLock(evidenceId);

        Evidence newEvidence = createEvidence(evidence);
        ReadingEvidence newReadingEvidence = createReadingEvidence(newEvidence, title, author, content, page);
        readingEvidencePersistencePort.saveReadingEvidence(newReadingEvidence);
    }

    /**
     * 수정 정보를 반영하여 새로운 {@link Evidence} 객체를 생성합니다.
     * <p>검토 상태는 {@code PENDING}으로 초기화되며, 수정 시각이 갱신됩니다.
     * @param evidence 기존 증빙자료
     * @return 수정된 Evidence 객체
     */
    private Evidence createEvidence(Evidence evidence) {
        return Evidence.builder()
                .id(evidence.getId())
                .score(evidence.getScore())
                .reviewStatus(ReviewStatus.PENDING)
                .evidenceType(evidence.getEvidenceType())
                .createdAt(evidence.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * 입력된 독서 정보를 기반으로 새로운 {@link ReadingEvidence} 객체를 생성합니다.
     * @param evidence 연관된 Evidence 객체
     * @param title 수정된 제목
     * @param author 수정된 저자
     * @param content 수정된 독서 내용
     * @param page 수정된 페이지 수
     * @return 생성된 ReadingEvidence 객체
     */
    private ReadingEvidence createReadingEvidence(Evidence evidence, String title, String author, String content, int page) {
        return ReadingEvidence.builder()
                .id(evidence)
                .author(author)
                .title(title)
                .page(page)
                .content(content)
                .build();
    }
}