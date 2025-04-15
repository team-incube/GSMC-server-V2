package team.incude.gsmc.v2.domain.member.application.port;

import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

@Port(direction = PortDirection.OUTBOUND)
public interface MemberPersistencePort {
    Boolean existsMemberByEmail(String email);

    Member saveMember(Member member);

    Member findMemberByEmail(String email);

    Member findMemberById(Long memberId);
}