package team.incube.gsmc.v2.domain.category.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gsmc.v2.domain.category.application.port.CategoryPersistencePort;
import team.incube.gsmc.v2.domain.category.application.usecase.GetAllCategoriesUseCase;
import team.incube.gsmc.v2.domain.category.domain.Category;
import team.incube.gsmc.v2.domain.category.presentation.data.response.GetAllCategoriesResponse;
import team.incube.gsmc.v2.domain.category.presentation.data.response.GetCategoryResponse;

import java.util.List;

/**
 * 모든 카테고리 목록을 조회하는 유스케이스 구현체입니다.
 * <p>{@link GetAllCategoriesUseCase}를 구현하며, {@link CategoryPersistencePort}를 통해
 * 영속성 계층에서 카테고리 데이터를 조회하고 응답 DTO로 변환합니다.
 * <p>이 서비스는 읽기 전용 트랜잭션으로 실행되며,
 * 영속성 어댑터에서 제공하는 캐싱 기능을 통해 성능을 최적화합니다.
 * @author snowykte0426
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetAllCategoriesService implements GetAllCategoriesUseCase {

    private final CategoryPersistencePort categoryPersistencePort;

    /**
     * 모든 카테고리 목록을 조회합니다.
     * @return 전체 카테고리 정보를 담은 응답 DTO
     */
    @Override
    public GetAllCategoriesResponse execute() {
        List<Category> categories = categoryPersistencePort.findAllCategory();
        List<GetCategoryResponse> categoryResponses = categories.stream()
                .map(this::mapToResponse)
                .toList();
        
        return new GetAllCategoriesResponse(categoryResponses);
    }

    private GetCategoryResponse mapToResponse(Category category) {
        return new GetCategoryResponse(
                category.getId(),
                category.getName(),
                category.getMaximumValue(),
                category.getWeight(),
                category.getIsEvidenceRequired(),
                category.getKoreanName()
        );
    }
}