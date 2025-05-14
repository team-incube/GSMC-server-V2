package team.incude.gsmc.v2.domain.auth.application.usecase;

/**
 * 사용자 회원가입을 위한 유스케이스 인터페이스입니다.
 * <p>사용자가 입력한 이름, 이메일, 비밀번호를 기반으로 회원가입을 처리합니다.
 * 이 유스케이스는 주로 Web 어댑터를 통해 호출되며, {@code AuthApplicationPort}에서 이 유스케이스를 위임받아 실행됩니다.
 * @author jihoonwjj
 */
public interface SignUpUseCase {
    void execute(String name, String email, String password);
}
