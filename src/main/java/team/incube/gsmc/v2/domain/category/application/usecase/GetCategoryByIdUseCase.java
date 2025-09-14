package team.incube.gsmc.v2.domain.category.application.usecase;

import team.incube.gsmc.v2.domain.category.presentation.data.response.GetCategoryResponse;

/**
 * ID를 기준으로 특정 카테고리를 조회하는 유스케이스 인터페이스입니다.
 * <p>카테고리 ID를 입력받아 해당하는 카테고리 정보를 반환하며,
 * 존재하지 않는 ID인 경우 {@code CategoryNotFoundException}을 발생시킵니다.
 * <p>주로 특정 카테고리의 상세 정보 조회나 점수 입력 시 카테고리 검증을 위해 사용됩니다.
 * @author snowykte0426
 */
public interface GetCategoryByIdUseCase {
    /**
     * ID를 기준으로 특정 카테고리를 조회합니다.
     * @param id 조회할 카테고리의 ID
     * @return 조회된 카테고리 정보를 담은 응답 DTO
     * @throws team.incube.gsmc.v2.domain.category.exception.CategoryNotFoundException 카테고리를 찾을 수 없는 경우
     */
    GetCategoryResponse execute(Long id);
}