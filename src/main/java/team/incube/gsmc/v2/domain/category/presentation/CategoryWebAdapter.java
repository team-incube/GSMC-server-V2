package team.incube.gsmc.v2.domain.category.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 카테고리 관련 HTTP 요청을 처리하는 Web 어댑터 클래스입니다.
 * <p>카테고리 조회, 생성, 수정, 삭제 기능을 제공합니다.
 * <p>제공하는 API:
 * <ul>
 *   <li>{@code GET /api/v2/category} - 모든 카테고리 조회</li>
 *   <li>{@code GET /api/v2/category/{id}} - 특정 카테고리 검색</li>
 * </ul>
 * @author YourName
 */
@RestController
@RequestMapping("/api/v2/category")
@RequiredArgsConstructor
public class CategoryWebAdapter {
}