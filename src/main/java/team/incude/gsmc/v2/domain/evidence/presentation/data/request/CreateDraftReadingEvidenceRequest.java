package team.incude.gsmc.v2.domain.evidence.presentation.data.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * 독서 증빙자료 임시저장을 위한 요청 DTO입니다.
 * <p>책 제목, 저자, 페이지 수, 읽은 내용 등을 포함하여
 * 추후 정식 증빙자료로 등록하기 위한 임시 데이터를 저장합니다.
 * <p>클라이언트는 해당 데이터를 바탕으로 추후 저장 또는 수정을 진행할 수 있습니다.
 * @param draftId 기존 임시저장 ID (수정 시 사용, 생성 시 null 가능)
 * @param title 책 제목
 * @param author 저자명
 * @param page 책 페이지 수
 * @param content 독서 내용 요약
 * @author suuuuuuminnnnnn
 */
public record CreateDraftReadingEvidenceRequest(
        UUID draftId,
        String title,
        String author,
        Integer page,
        String content
) {
}
