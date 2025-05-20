package team.incude.gsmc.v2.global.security.jwt.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.global.security.exception.MemberUnauthorizedException;

/**
 * 현재 로그인한 사용자의 정보를 제공하는 컴포넌트입니다.
 * <p>Spring Security의 {@link SecurityContextHolder}를 이용하여 인증된 사용자의
 * 이메일을 추출하고, 이를 기반으로 실제 {@link Member} 엔티티를 조회합니다.
 *
 * <p>주요 기능:
 * <ul>
 *   <li>현재 사용자 이메일 추출</li>
 *   <li>회원 정보 조회</li>
 *   <li>비정상 접근 시 예외 처리</li>
 * </ul>
 *
 * <p>이 컴포넌트는 인증이 필요한 서비스나 컨트롤러에서 사용자의 정보를 획득할 때 사용됩니다.
 * 인증되지 않은 사용자가 접근하려 할 경우 {@link MemberUnauthorizedException}을 발생시킵니다.
 *
 * @author jihoonwjj
 */

@Component
@RequiredArgsConstructor
public class CurrentMemberProvider {

    private final MemberPersistencePort memberPersistencePort;

    /**
     * 현재 인증된 사용자의 {@link Member} 객체를 반환합니다.
     *
     * @return 현재 로그인된 사용자의 Member 도메인 객체
     * @throws MemberUnauthorizedException 인증 정보가 없거나 잘못된 경우
     */

    public Member getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof String email) {
            return memberPersistencePort.findMemberByEmail(email);
        }

        throw new MemberUnauthorizedException();
    }
}