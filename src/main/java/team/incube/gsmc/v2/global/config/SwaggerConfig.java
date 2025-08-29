package team.incube.gsmc.v2.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger/OpenAPI 문서화 설정을 담당하는 구성 클래스입니다.
 * <p>API 문서의 기본 정보, 인증 방식, 서버 정보를 설정하며,
 * 개발 및 운영 환경에서 API 문서를 제공합니다.
 * <p>주요 구성 요소:
 * <ul>
 *   <li>API 기본 정보 (제목, 버전, 설명, 라이선스)</li>
 *   <li>JWT Bearer 토큰 인증 스키마</li>
 *   <li>개발 및 운영 서버 정보</li>
 *   <li>글로벌 보안 요구사항</li>
 * </ul>
 * <p>접속 경로:
 * <ul>
 *   <li>Swagger UI: {@code /swagger-ui/index.html}</li>
 *   <li>API 문서 JSON: {@code /v3/api-docs}</li>
 * </ul>
 * @author snowykte0426
 */
@Configuration
public class SwaggerConfig {

    /**
     * OpenAPI 3.0 문서 설정을 구성합니다.
     * <p>GSMC 서버의 API 문서를 생성하며, JWT 인증을 포함한 보안 설정을 적용합니다.
     * 개발 및 운영 환경의 서버 정보를 제공하여 다양한 환경에서 테스트할 수 있습니다.
     * 
     * @return 구성된 OpenAPI 객체
     */
    @Bean
    public OpenAPI openAPI() {
        String jwtSchemeName = "Bearer Authentication";
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(jwtSchemeName);
        
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("JWT 토큰을 입력하세요. 'Bearer ' 접두사는 자동으로 추가됩니다."));

        return new OpenAPI()
                .info(new Info()
                        .title("GSMC Server V2 API")
                        .description("경소마고 학생 관리 및 점수 관리 시스템 API 문서입니다.\n\n" +
                                "이 API는 학생 정보 관리, 점수 카테고리 관리, 증빙 자료 관리, 인증 등의 기능을 제공합니다.\n\n" +
                                "### 인증 방법\n" +
                                "대부분의 API는 JWT 토큰을 통한 인증이 필요합니다. " +
                                "우측 상단의 'Authorize' 버튼을 클릭하여 토큰을 입력하세요.\n\n" +
                                "### 주요 기능\n" +
                                "- **인증 (Auth)**: 로그인, 회원가입, 토큰 갱신\n" +
                                "- **카테고리 (Category)**: 점수 카테고리 조회\n" +
                                "- **학생 관리 (Member)**: 학생 정보 조회 및 관리\n" +
                                "- **증빙 자료 (Evidence)**: 증빙 자료 업로드 및 관리\n" +
                                "- **점수 관리 (Score)**: 학생 점수 조회 및 관리")
                        .version("2.0.0")
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("로컬 개발 서버"),
                        new Server()
                                .url("https://api.gsmc.example.com")
                                .description("운영 서버")))
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}