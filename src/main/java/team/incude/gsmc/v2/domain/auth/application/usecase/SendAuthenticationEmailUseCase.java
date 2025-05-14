package team.incude.gsmc.v2.domain.auth.application.usecase;

/**
 * 이메일 인증을 위한 유스케이스 인터페이스입니다.
 * <p>이메일 주소를 입력받아 해당 주소로 인증 이메일을 전송하는 기능을 정의합니다.
 * 이 유스케이스는 주로 Web 어댑터를 통해 호출되며, {@code AuthApplicationPort}에서 이 유스케이스를 위임받아 실행됩니다.
 * @author snowykte0426
 */
public interface SendAuthenticationEmailUseCase {
    void execute(String email);
}
