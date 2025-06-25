package team.incube.gsmc.v2.domain.evidence.application.usecase;

import team.incube.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incube.gsmc.v2.domain.evidence.presentation.data.response.GetEvidencesResponse;

/**
 * 학생 코드, 증빙자료 제목, 증빙자료 타입을 기준으로 필터링하여 증빙자료를 조회하는 유스케이스 인터페이스입니다.
 * <p>관리자는 이 유스케이스를 통해 특정 학생의 증빙자료 중 조건에 맞는 항목만 선별하여 확인할 수 있습니다.
 * <p>조건은 모두 선택적으로 사용 가능하며, 일부 조건만 지정해도 조회할 수 있습니다.
 * 반환값은 {@link GetEvidencesResponse} 형태이며, 검색된 증빙자료 목록을 포함합니다.
 * @author suuuuuuminnnnnn
 */
public interface FindEvidenceByTitleAndTypeUseCase {
    GetEvidencesResponse execute(String title, EvidenceType evidenceType);
}
