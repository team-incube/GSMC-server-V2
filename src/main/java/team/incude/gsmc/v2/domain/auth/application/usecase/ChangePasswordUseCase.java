package team.incude.gsmc.v2.domain.auth.application.usecase;

/**
 * 비밀번호 변경 유스케이스를 정의하는 인터페이스입니다.
 * <p>이메일을 통해 사용자를 식별하고, 해당 사용자의 비밀번호를 새로운 값으로 변경합니다.
 * 인증 또는 사용자 본인 확인이 선행되어야 하며, 도메인 계층에서 비즈니스 규칙을 검증한 후 실행됩니다.
 * 이 포트는 주로 Web 어댑터를 통해 호출되며, {@code AuthApplicationPort}에서 이 유스케이스를 위임받아 실행합니다.
 * @author snowykte0426
 */
public interface ChangePasswordUseCase {
    void execute(String email, String newPassword);
}
