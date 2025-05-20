package team.incude.gsmc.v2.domain.evidence.presentation.data.response;

import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;

/**
 * 기타 증빙자료 단건 조회 응답 DTO입니다.
 * <p>기타 유형 증빙자료의 ID, 파일 URI, 증빙자료 타입, 검토 상태, 카테고리 정보를 포함하여 반환합니다.
 * 클라이언트는 이 정보를 기반으로 증빙자료 상세 페이지를 구성할 수 있습니다.
 * @param id 증빙자료 ID
 * @param fileUri 증빙자료 파일 URI
 * @param evidenceType 증빙자료 타입 (기타 유형)
 * @param status 검토 상태 (PENDING, APPROVED, REJECTED 등)
 * @param categoryName 카테고리 이름
 * @author suuuuuuminnnnnn
 */
public record GetOtherEvidenceResponse(
        Long id,
        String fileUri,
        EvidenceType evidenceType,
        ReviewStatus status,
        String categoryName
) {
}
