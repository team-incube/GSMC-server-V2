package team.incube.gsmc.v2.domain.category.application.usecase;

import team.incube.gsmc.v2.domain.category.presentation.data.response.GetAllCategoriesResponse;

/**
 * 모든 카테고리 목록을 조회하는 유스케이스 인터페이스입니다.
 * <p>시스템에 등록된 모든 점수 카테고리의 목록을 반환하며,
 * 클라이언트에서 카테고리별 점수 입력이나 조회 시 사용됩니다.
 * <p>이 유스케이스는 캐싱을 통해 성능을 최적화하며,
 * 카테고리 정보는 자주 변경되지 않는 참조 데이터의 특성을 가집니다.
 * @author snowykte0426
 */
public interface GetAllCategoriesUseCase {
    /**
     * 모든 카테고리 목록을 조회합니다.
     * @return 전체 카테고리 정보를 담은 응답 DTO
     */
    GetAllCategoriesResponse execute();
}