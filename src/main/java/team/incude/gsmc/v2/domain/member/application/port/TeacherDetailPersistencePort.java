package team.incude.gsmc.v2.domain.member.application.port;

import team.incude.gsmc.v2.domain.member.domain.HomeroomTeacherDetail;

public interface TeacherDetailPersistencePort {
    HomeroomTeacherDetail findTeacherDetailByEmail(String email);
}
