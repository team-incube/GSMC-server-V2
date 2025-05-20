package team.incude.gsmc.v2.global.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.member.domain.constant.MemberRole;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;
import team.incude.gsmc.v2.domain.auth.application.port.JwtPort;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.JwtIssueUseCase;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.JwtParserUseCase;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.JwtRefreshManagementUseCase;
import team.incude.gsmc.v2.global.security.jwt.data.TokenDto;

/**
 * JWT 관련 기능을 제공하는 어댑터 클래스입니다.
 * <p>{@link JwtPort}를 구현하며, JWT 발급, 파싱, 검증 및 리프레시 토큰 관리를 수행합니다.
 * <p>담당 기능:
 * <ul>
 *   <li>Access/Refresh 토큰 발급</li>
 *   <li>토큰 유효성 검사</li>
 *   <li>토큰에서 이메일 및 권한 정보 추출</li>
 *   <li>HTTP 요청에서 토큰 추출</li>
 *   <li>리프레시 토큰 삭제</li>
 * </ul>
 * 각 기능은 의존성 주입된 유스케이스 클래스에 위임됩니다.
 *
 * @author jihoonwjj
 */

@Adapter(direction = PortDirection.INBOUND)
@RequiredArgsConstructor
public class JwtAdapter implements JwtPort {

    private final JwtIssueUseCase jwtIssueUseCase;
    private final JwtParserUseCase jwtParserUseCase;
    private final JwtRefreshManagementUseCase jwtRefreshManagementUseCase;

    /**
     * 주어진 이메일과 권한을 기반으로 액세스토큰을 발급합니다.
     *
     * @param email 사용자 이메일
     * @param roles 사용자 권한
     * @return 발급된 액세스토큰 DTO
     */
    public TokenDto issueAccessToken(String email, MemberRole roles) {
        return jwtIssueUseCase.issueAccessToken(email, roles);
    }
    /**
     * 주어진 이메일을 기반으로 리프레시토큰을 발급합니다.
     *
     * @param email 사용자 이메일
     * @return 발급된 리프레시토큰 DTO
     */
    public TokenDto issueRefreshToken(String email) {
        return jwtIssueUseCase.issueRefreshToken(email);
    }
    /**
     * 주어진 액세스토큰의 유효성을 검증합니다.
     *
     * @param token 검증할 액세스토큰
     * @return 유효 여부
     */
    public Boolean validateAccessToken(String token) {
        return jwtParserUseCase.validateAccessToken(token);
    }
    /**
     * 주어진 리프레시토큰의 유효성을 검증합니다.
     *
     * @param token 검증할 리프레시토큰
     * @return 유효 여부
     */
    public Boolean validateRefreshToken(String token) {
        return jwtParserUseCase.validateRefreshToken(token);
    }
    /**
     * 액세스토큰으로부터 사용자 이메일을 추출합니다.
     *
     * @param token 액세스토큰
     * @return 추출된 이메일
     */
    public String getEmailFromAccessToken(String token) {
        return jwtParserUseCase.getEmailFromAccessToken(token);
    }
    /**
     * 리프레시토큰으로부터 사용자 이메일을 추출합니다.
     *
     * @param token 리프레시토큰
     * @return 추출된 이메일
     */
    public String getEmailFromRefreshToken(String token) {
        return jwtParserUseCase.getEmailFromRefreshToken(token);
    }
    /**
     * 액세스토큰으로부터 사용자 권한(Role)을 추출합니다.
     *
     * @param token 액세스토큰
     * @return 추출된 사용자 권한
     */
    public MemberRole getRolesFromAccessToken(String token) {
        return jwtParserUseCase.getRolesFromAccessToken(token);
    }
    /**
     * HTTP 요청 헤더에서 JWT를 추출합니다.
     *
     * @param request HTTP 요청 객체
     * @return 추출된 JWT 문자열
     */
    public String resolveToken(HttpServletRequest request) {
        return jwtParserUseCase.resolveToken(request);
    }
    /**
     * 주어진 리프레시토큰을 삭제합니다.
     *
     * @param refreshToken 삭제할 리프레시토큰
     */
    public void deleteRefreshToken(String refreshToken) {
        jwtRefreshManagementUseCase.deleteRefreshToken(refreshToken);
    }
}
