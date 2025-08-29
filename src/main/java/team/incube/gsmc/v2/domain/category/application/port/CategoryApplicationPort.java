package team.incube.gsmc.v2.domain.category.application.port;

import team.incube.gsmc.v2.domain.category.presentation.data.response.GetAllCategoriesResponse;
import team.incube.gsmc.v2.domain.category.presentation.data.response.GetCategoryResponse;
import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.port.Port;

/**
 * 카테고리 도메인의 인바운드 포트 인터페이스입니다.
 * <p>웹 어댑터에서 카테고리 관련 비즈니스 로직을 호출하기 위한 진입점을 제공합니다.
 * 카테고리 조회 관련 유스케이스들을 캡슐화하여 외부 계층과의 결합도를 낮춥니다.
 * <p>제공 기능:
 * <ul>
 *   <li>{@code findAllCategories()} - 모든 카테고리 목록 조회</li>
 *   <li>{@code findCategoryById(Long id)} - ID로 특정 카테고리 조회</li>
 * </ul>
 * @author snowykte0426
 */
@Port(direction = PortDirection.INBOUND)
public interface CategoryApplicationPort {
    
    /**
     * 모든 카테고리 목록을 조회합니다.
     * @return 전체 카테고리 정보를 담은 응답 DTO
     */
    GetAllCategoriesResponse findAllCategories();
    
    /**
     * ID를 기준으로 특정 카테고리를 조회합니다.
     * @param id 조회할 카테고리의 ID
     * @return 조회된 카테고리 정보를 담은 응답 DTO
     * @throws team.incube.gsmc.v2.domain.category.exception.CategoryNotFoundException 카테고리를 찾을 수 없는 경우
     */
    GetCategoryResponse findCategoryById(Long id);
}