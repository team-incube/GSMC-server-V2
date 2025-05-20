package team.incude.gsmc.v2.domain.evidence.application.usecase;

import team.incude.gsmc.v2.domain.evidence.presentation.data.response.CreateDraftEvidenceResponse;

import java.util.UUID;

/**
 * 독서 증빙자료 임시저장을 위한 유스케이스 인터페이스입니다.
 * <p>책 제목, 저자, 페이지 수, 독서 내용을 기반으로 임시저장을 생성하거나 갱신합니다.
 * <p>{@code draftId}가 존재할 경우 기존 임시저장을 덮어쓰며, null일 경우 새로 생성됩니다.
 * 이 유스케이스는 작성 중인 독서 증빙자료를 정식 제출 전에 저장해두고,
 * 이후에 이어서 수정하거나 제출할 수 있도록 지원합니다.
 * @author suuuuuuminnnnnn
 */
public interface CreateDraftReadingEvidenceUseCase {
    CreateDraftEvidenceResponse execute(UUID draftId, String title, String author, Integer page, String content);
}
