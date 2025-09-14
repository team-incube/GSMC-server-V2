package team.incube.gsmc.v2.domain.category.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gsmc.v2.domain.category.application.port.CategoryPersistencePort;
import team.incube.gsmc.v2.domain.category.application.usecase.GetCategoryByIdUseCase;
import team.incube.gsmc.v2.domain.category.domain.Category;
import team.incube.gsmc.v2.domain.category.exception.CategoryNotFoundException;
import team.incube.gsmc.v2.domain.category.presentation.data.response.GetCategoryResponse;

/**
 * ID를 기준으로 특정 카테고리를 조회하는 유스케이스 구현체입니다.
 * <p>{@link GetCategoryByIdUseCase}를 구현하며, {@link CategoryPersistencePort}를 통해
 * 영속성 계층에서 카테고리 데이터를 조회하고 응답 DTO로 변환합니다.
 * <p>카테고리가 존재하지 않는 경우 {@link CategoryNotFoundException}을 발생시키며,
 * 이는 글로벌 예외 핸들러를 통해 적절한 HTTP 응답으로 변환됩니다.
 * @author snowykte0426
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetCategoryByIdService implements GetCategoryByIdUseCase {

    private final CategoryPersistencePort categoryPersistencePort;

    /**
     * ID를 기준으로 특정 카테고리를 조회합니다.
     * @param id 조회할 카테고리의 ID
     * @return 조회된 카테고리 정보를 담은 응답 DTO
     * @throws CategoryNotFoundException 카테고리를 찾을 수 없는 경우
     */
    @Override
    public GetCategoryResponse execute(Long id) {
        Category category = categoryPersistencePort.findCategoryById(id)
                .orElseThrow(CategoryNotFoundException::new);
        
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