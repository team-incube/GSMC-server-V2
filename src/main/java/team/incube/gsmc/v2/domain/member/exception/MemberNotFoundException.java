package team.incube.gsmc.v2.domain.member.exception;

import team.incube.gsmc.v2.global.error.ErrorCode;
import team.incube.gsmc.v2.global.error.exception.GsmcException;

/**
 * 사용자(Member) 정보를 찾을 수 없을 때 발생하는 예외입니다.
 * <p>이메일, 학생 코드 등으로 사용자 조회 시 해당 정보가 존재하지 않을 경우 발생하며,
 * {@link ErrorCode#MEMBER_NOT_FOUND}에 매핑되어 클라이언트에 적절한 오류 메시지를 전달합니다.
 * <p>주로 {@code MemberPersistencePort} 또는 {@code StudentDetailPersistencePort}에서 조회 실패 시 사용됩니다.
 * @author snowykte0426
 */
public class MemberNotFoundException extends GsmcException {
    public MemberNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}