package team.incube.gsmc.v2.domain.auth.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gsmc.v2.domain.auth.application.port.AuthenticationPersistencePort;
import team.incube.gsmc.v2.domain.auth.exception.MemberForbiddenException;
import team.incube.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incube.gsmc.v2.domain.auth.application.usecase.ChangePasswordUseCase;
import team.incube.gsmc.v2.domain.member.domain.Member;

/**
 * 비밀번호 변경 유스케이스의 구현체입니다.
 * <p>이메일을 통해 사용자를 조회하고, 인증 상태가 확인된 경우 새 비밀번호로 갱신합니다.
 * <p>비밀번호는 BCrypt를 통해 암호화된 후 저장되며, 인증되지 않은 사용자에 대해서는 {@link MemberForbiddenException}을 발생시킵니다.
 * @see ChangePasswordUseCase
 * @see AuthenticationPersistencePort
 * @see MemberPersistencePort
 * @see BCryptPasswordEncoder
 * @see MemberForbiddenException
 * @author jihoonwjj
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ChangePasswordService implements ChangePasswordUseCase {

    private final AuthenticationPersistencePort authenticationPersistencePort;
    private final MemberPersistencePort memberPersistencePort;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 이메일을 통해 사용자의 인증 여부를 확인한 뒤, 비밀번호를 새 값으로 변경합니다.
     * @param email 사용자 이메일
     * @param newPassword 새 비밀번호 (BCrypt로 암호화되어 저장됨)
     * @throws MemberForbiddenException 이메일 인증이 완료되지 않은 사용자일 경우
     */
    public void execute(String email, String newPassword) {
        Member member = memberPersistencePort.findMemberByEmail(email);
        if (!authenticationPersistencePort.findAuthenticationByEmail(member.getEmail()).getVerified()) {
            throw new MemberForbiddenException();
        }

        memberPersistencePort.updateMemberPassword(member.getId(), bCryptPasswordEncoder.encode(newPassword));
    }
}
