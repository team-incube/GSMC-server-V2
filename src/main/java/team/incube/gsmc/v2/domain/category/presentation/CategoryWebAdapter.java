package team.incube.gsmc.v2.domain.category.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.incube.gsmc.v2.domain.category.application.port.CategoryApplicationPort;
import team.incube.gsmc.v2.domain.category.presentation.data.response.GetAllCategoriesResponse;
import team.incube.gsmc.v2.domain.category.presentation.data.response.GetCategoryResponse;
import team.incube.gsmc.v2.global.error.data.response.ErrorResponse;


/**
 * 카테고리 관련 HTTP 요청을 처리하는 Web 어댑터 클래스입니다.
 * <p>카테고리 조회 기능을 제공하며, 시스템에 등록된 점수 카테고리 정보를 클라이언트에 제공합니다.
 * <p>제공하는 API:
 * <ul>
 *   <li>{@code GET /api/v2/category} - 모든 카테고리 조회</li>
 *   <li>{@code GET /api/v2/category/{id}} - 특정 카테고리 조회</li>
 * </ul>
 * @author snowykte0426
 */
@Tag(name = "Category", description = "카테고리 관리 API")
@RestController
@RequestMapping("/api/v2/category")
@RequiredArgsConstructor
public class CategoryWebAdapter {

    private final CategoryApplicationPort categoryApplicationPort;

    /**
     * 모든 카테고리 목록을 조회합니다.
     * <p>시스템에 등록된 모든 점수 카테고리의 정보를 반환합니다.
     * 각 카테고리는 ID, 이름, 최대값, 가중치, 증빙 필요 여부, 한국어 이름을 포함합니다.
     * 
     * @return 전체 카테고리 목록을 담은 응답 객체
     */
    @Operation(
        summary = "모든 카테고리 조회",
        description = "시스템에 등록된 모든 점수 카테고리의 목록을 조회합니다. " +
                     "각 카테고리는 ID, 영문명, 한국어명, 최대 점수, 가중치, 증빙 필요 여부를 포함합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "카테고리 목록 조회 성공",
            content = @Content(schema = @Schema(implementation = GetAllCategoriesResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "서버 내부 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping
    public ResponseEntity<GetAllCategoriesResponse> getAllCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryApplicationPort.findAllCategories());
    }

    /**
     * 특정 ID에 해당하는 카테고리 정보를 조회합니다.
     * <p>존재하지 않는 ID인 경우 404 Not Found 에러가 반환됩니다.
     * 
     * @param id 조회할 카테고리의 ID
     * @return 조회된 카테고리 정보를 담은 응답 객체
     */
    @Operation(
        summary = "특정 카테고리 조회",
        description = "카테고리 ID를 사용하여 특정 카테고리의 상세 정보를 조회합니다. " +
                     "존재하지 않는 ID인 경우 404 Not Found 에러가 반환됩니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "카테고리 조회 성공",
            content = @Content(schema = @Schema(implementation = GetCategoryResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "카테고리를 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "서버 내부 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<GetCategoryResponse> getCategoryById(
        @Parameter(description = "조회할 카테고리의 ID", example = "1") 
        @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryApplicationPort.findCategoryById(id));
    }
}