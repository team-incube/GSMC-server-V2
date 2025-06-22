package team.incube.gsmc.v2.domain.auth.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.incube.gsmc.v2.domain.auth.application.port.AuthApplicationPort;
import team.incube.gsmc.v2.domain.auth.presentation.data.request.*;
import team.incube.gsmc.v2.domain.auth.presentation.data.response.AuthTokenResponse;

/**
 * 인증 관련 HTTP 요청을 처리하는 Web 어댑터 클래스입니다.
 * <p>회원 가입, 로그인, 토큰 갱신, 이메일 인증, 비밀번호 변경 등의 요청을 수신하고
 * {@link AuthApplicationPort}를 통해 도메인 유스케이스를 실행합니다.
 * <p>기본 경로: {@code /api/v2/auth}
 * @author jihoonwjj, snowykte0426
 */
@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
@RestController
public class AuthWebAdapter {

    private final AuthApplicationPort authApplicationPort;

    /**
     * 로그인 요청을 처리합니다.
     * @param request 로그인 요청 (이메일, 비밀번호 포함)
     * @return 액세스 및 리프레시 토큰
     */
    @PostMapping("/signin")
    public ResponseEntity<AuthTokenResponse> signIn(@Valid @RequestBody SignInRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(authApplicationPort.signIn(request.email(), request.password()));
    }

    /**
     * 회원 가입 요청을 처리합니다.
     * @param request 회원 가입 요청 (이름, 이메일, 비밀번호 포함)
     * @return 201 CREATED 응답
     */
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest request) {
        authApplicationPort.signUp(request.name(), request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 리프레시 토큰을 이용한 액세스 토큰 갱신 요청을 처리합니다.
     * @param request 리프레시 토큰 포함 요청
     * @return 새로운 액세스/리프레시 토큰
     */
    @PutMapping("/refresh")
    public ResponseEntity<AuthTokenResponse> refreshToken(@Valid @RequestBody RefreshRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(authApplicationPort.refresh(request.refreshToken()));
    }

    /**
     * 이메일 인증 코드 검증 요청을 처리합니다.
     * @param request 인증 코드 요청
     * @return 204 No Content
     */
    @PatchMapping("/verify-email")
    public ResponseEntity<Void> verifyEmail(@Valid @RequestBody VerificationCodeRequest request) {
        authApplicationPort.verifyEmail(request.code());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 이메일 인증을 위한 인증 코드 발송 요청을 처리합니다.
     * @param request 인증 이메일 요청
     * @return 204 No Content
     */
    @PostMapping("/send-email")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> sendAuthenticationEmail(@Valid @RequestBody SendEmailRequest request) {
        authApplicationPort.sendAuthenticationEmail(request.email());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 비밀번호 변경 요청을 처리합니다.
     * @param request 비밀번호 변경 요청 (이메일, 새 비밀번호 포함)
     * @return 204 No Content
     */
    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        authApplicationPort.changePassword(request.email(), request.password());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
