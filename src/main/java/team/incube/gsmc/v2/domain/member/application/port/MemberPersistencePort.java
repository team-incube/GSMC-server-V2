package team.incube.gsmc.v2.domain.member.application.port;

import team.incube.gsmc.v2.domain.member.domain.Member;
import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.port.Port;

/**
 * 사용자(Member) 도메인의 영속성 계층과 통신하는 포트 인터페이스입니다.
 * <p>이 인터페이스는 회원 정보를 조회, 저장, 수정하는 기능을 추상화하며,
 * 도메인 계층이 데이터베이스 세부 구현에 의존하지 않도록 분리합니다.
 * <p>{@code @Port(direction = PortDirection.OUTBOUND)}로 지정되어 외부 시스템(SPI)으로의 호출을 의미합니다.
 * <p>주요 기능:
 * <ul>
 *   <li>{@code existsMemberByEmail} - 이메일 존재 여부 확인</li>
 *   <li>{@code saveMember} - 회원 저장</li>
 *   <li>{@code findMemberByEmail} - 이메일로 회원 조회</li>
 *   <li>{@code findMemberByStudentDetailStudentCode} - 학생 코드로 회원 조회</li>
 *   <li>{@code updateMemberPassword} - 비밀번호 수정</li>
 * </ul>
 * @author snowykte0426, jihoonwjj
 */
@Port(direction = PortDirection.OUTBOUND)
public interface MemberPersistencePort {
    Boolean existsMemberByEmail(String email);

    Member saveMember(Member member);

    Member findMemberByEmail(String email);

    void updateMemberPassword(Long memberId, String newPassword);
}