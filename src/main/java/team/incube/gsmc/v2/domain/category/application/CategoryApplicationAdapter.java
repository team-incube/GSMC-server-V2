package team.incube.gsmc.v2.domain.category.application;

import lombok.RequiredArgsConstructor;
import team.incube.gsmc.v2.domain.category.application.port.CategoryApplicationPort;
import team.incube.gsmc.v2.domain.category.application.usecase.GetAllCategoriesUseCase;
import team.incube.gsmc.v2.domain.category.application.usecase.GetCategoryByIdUseCase;
import team.incube.gsmc.v2.domain.category.presentation.data.response.GetAllCategoriesResponse;
import team.incube.gsmc.v2.domain.category.presentation.data.response.GetCategoryResponse;
import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.adapter.Adapter;

/**
 * 카테고리 도메인의 애플리케이션 어댑터 구현체입니다.
 * <p>{@link CategoryApplicationPort}를 구현하며, 각각의 유스케이스들에게 작업을 위임합니다.
 * 이 어댑터는 웹 계층과 애플리케이션 계층 간의 중개 역할을 수행하며,
 * 헥사고날 아키텍처의 포트-어댑터 패턴을 준수합니다.
 * <p>의존성:
 * <ul>
 *   <li>{@link GetAllCategoriesUseCase} - 모든 카테고리 목록 조회</li>
 *   <li>{@link GetCategoryByIdUseCase} - ID로 특정 카테고리 조회</li>
 * </ul>
 * @author snowykte0426
 */
@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class CategoryApplicationAdapter implements CategoryApplicationPort {
    
    private final GetAllCategoriesUseCase getAllCategoriesUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    
    /**
     * 모든 카테고리 목록을 조회합니다.
     * @return 전체 카테고리 정보를 담은 응답 DTO
     */
    @Override
    public GetAllCategoriesResponse findAllCategories() {
        return getAllCategoriesUseCase.execute();
    }
    
    /**
     * ID를 기준으로 특정 카테고리를 조회합니다.
     * @param id 조회할 카테고리의 ID
     * @return 조회된 카테고리 정보를 담은 응답 DTO
     * @throws team.incube.gsmc.v2.domain.category.exception.CategoryNotFoundException 카테고리를 찾을 수 없는 경우
     */
    @Override
    public GetCategoryResponse findCategoryById(Long id) {
        return getCategoryByIdUseCase.execute(id);
    }
}