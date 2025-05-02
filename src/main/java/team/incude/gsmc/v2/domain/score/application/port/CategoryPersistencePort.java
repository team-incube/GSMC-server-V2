package team.incude.gsmc.v2.domain.score.application.port;

import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.List;

/**
 * 점수 분류(Category) 도메인의 영속성 계층과의 통신을 담당하는 포트 인터페이스입니다.
 * <p>카테고리 이름 기반 조회 및 전체 카테고리 목록 조회 기능을 제공합니다.
 * 도메인 계층과 외부 저장소 간의 의존성을 분리하기 위해 사용됩니다.
 * <p>{@code @Port(direction = PortDirection.OUTBOUND)}로 선언되며,
 * 구현체는 보통 JPA 또는 외부 시스템과 연동됩니다.
 * 주요 기능:
 * <ul>
 *   <li>{@code findCategoryByName} - 카테고리 이름으로 단건 조회</li>
 *   <li>{@code findAllCategory} - 전체 카테고리 목록 조회</li>
 * </ul>
 * @author snowykte0426
 */
@Port(direction = PortDirection.OUTBOUND)
public interface CategoryPersistencePort {
    Category findCategoryByName(String name);

    List<Category> findAllCategory();
}