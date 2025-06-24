package team.incube.gsmc.v2.domain.evidence.presentation.data.response;

import java.util.List;

/**
 * 사용자의 전체 증빙자료 목록을 반환하는 응답 DTO입니다.
 * <p>전공 활동, 인문 활동, 독서, 기타 증빙자료를 구분하여 리스트로 제공합니다.
 * 클라이언트는 이 응답을 통해 카테고리별로 증빙자료를 분리하여 화면에 출력할 수 있습니다.
 * 이 DTO는 {@link GetActivityEvidenceResponse}, {@link GetReadingEvidenceResponse}, {@link GetOtherEvidenceResponse}로 구성되어 있습니다.
 * @param majorActivityEvidence 전공 활동 증빙자료 목록
 * @param humanitiesActivityEvidence 인문 활동 증빙자료 목록
 * @param readingEvidence 독서 증빙자료 목록
 * @param otherEvidence 기타 증빙자료 목록
 * @author suuuuuuminnnnnn
 */
public record GetEvidencesResponse(
        List<GetActivityEvidenceResponse> majorActivityEvidence,
        List<GetActivityEvidenceResponse> humanitiesActivityEvidence,
        List<GetReadingEvidenceResponse> readingEvidence,
        List<GetOtherEvidenceResponse> otherEvidence
) {
}
