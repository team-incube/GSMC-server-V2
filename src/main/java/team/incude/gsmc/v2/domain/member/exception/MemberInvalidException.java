package team.incude.gsmc.v2.domain.member.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

/**
 * 유효하지 않은 학생-사용자 연관 관계가 감지되었을 때 발생하는 예외입니다.
 * <p>예를 들어 학생 상세 정보가 존재하지만, 이에 연결된 사용자 정보가 없거나 불일치하는 경우 발생합니다.
 * {@link ErrorCode#STUDENT_MEMBER_INVALID}에 매핑되어 클라이언트에 적절한 오류 메시지를 전달합니다.
 * <p>주로 {@code StudentDetailPersistencePort} 또는 {@code MemberPersistencePort}에서 연관된 회원 조회 실패 시 사용됩니다.
 * @author jihoonwjj
 */
public class MemberInvalidException extends GsmcException {
    public MemberInvalidException() {
        super(ErrorCode.STUDENT_MEMBER_INVALID);
    }
}
