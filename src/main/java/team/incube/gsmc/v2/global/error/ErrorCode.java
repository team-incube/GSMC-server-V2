package team.incube.gsmc.v2.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import team.incube.gsmc.v2.global.error.handler.GlobalExceptionHandler;

/**
 * 애플리케이션 전반에서 사용되는 표준 오류 코드 열거형입니다.
 * <p>각 오류 항목은 오류 메시지와 HTTP 상태 코드를 함께 정의하며,
 * {@link GlobalExceptionHandler}에서 참조되어 일관된 오류 응답을 제공합니다.
 * <p>분류:
 * <ul>
 *   <li>AWS S3: 업로드/삭제/파일 유효성 오류</li>
 *   <li>Certificate: 자격증 도메인 관련 오류</li>
 *   <li>Score: 점수 유효성, 증빙자료 요구 등 도메인 제약 오류</li>
 *   <li>Member/Auth: 사용자 인증/인가 및 관련 서비스 오류</li>
 *   <li>Evidence: 각 증빙자료 유형별 처리 오류</li>
 *   <li>Sheet: 시트 생성 실패</li>
 * </ul>
 * 이 Enum은 API 오류 응답의 표준화 및 유지보수성을 향상시키기 위해 사용됩니다.
 * @author snowykte0426, jihoonwjj, suuuuuuminnnnnn
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    // AWS S3
    S3_UPLOAD_FAILED("AWS S3 Upload Failed", HttpStatus.INTERNAL_SERVER_ERROR.value()),
    S3_DELETE_FAILED("AWS S3 Delete Failed", HttpStatus.INTERNAL_SERVER_ERROR.value()),
    FILE_NAME_TOO_LONG("File Name Too Long", HttpStatus.BAD_REQUEST.value()),
    FILE_IS_EMPTY("File is Empty", HttpStatus.BAD_REQUEST.value()),
    INVALID_FILE_URI("Invalid Stored File URI", HttpStatus.INTERNAL_SERVER_ERROR.value()),

    // Certificate
    CERTIFICATE_NOT_FOUND("Certificate Not Found", HttpStatus.NOT_FOUND.value()),
    CERTIFICATE_NOT_BELONG_TO_MEMBER("Certificate Not Belong To Member", HttpStatus.FORBIDDEN.value()),
    DUPLICATE_CERTIFICATE("Duplicate Certificate", HttpStatus.CONFLICT.value()),
    CERTIFICATE_CATEGORY_MISMATCH("Certificate Category Mismatch", HttpStatus.UNPROCESSABLE_ENTITY.value()),

    // Score
    SCORE_MAXIMUM_VALUE_EXCEEDED("Score Maximum Value Exceeded", HttpStatus.UNPROCESSABLE_ENTITY.value()),
    INVALID_SCORE_VALUE("Invalid Score Value", HttpStatus.UNPROCESSABLE_ENTITY.value()),
    REQUIRED_EVIDENCE_CATEGORY("Required Evidence Category", HttpStatus.CONFLICT.value()),
    STUDENT_CLASS_MISMATCH("Student Class Mismatch", HttpStatus.FORBIDDEN.value()),

    // Member
    MEMBER_NOT_FOUND("Member Not Found", HttpStatus.NOT_FOUND.value()),
    MEMBER_ALREADY_EXISTS("Member Already Exists", HttpStatus.CONFLICT.value()),

    // Category
    CATEGORY_NOT_FOUND("Category Not Found", HttpStatus.NOT_FOUND.value()),
    INVALID_CATEGORY("Invalid Category", HttpStatus.INTERNAL_SERVER_ERROR.value()),

    // Auth
    MEMBER_UNAUTHORIZED("Member Not Authorized", HttpStatus.UNAUTHORIZED.value()),
    MEMBER_FORBIDDEN("Member Forbidden", HttpStatus.FORBIDDEN.value()),
    REFRESH_TOKEN_INVALID("RefreshToken Expired Or Invalid", HttpStatus.UNAUTHORIZED.value()),
    EMAIL_AUTH_ATTEMPT_EXCEEDED("Email Auth Attempt Exceeded", HttpStatus.TOO_MANY_REQUESTS.value()),
    PASSWORD_INVALID("Password Invalid", HttpStatus.UNAUTHORIZED.value()),
    AUTHENTICATION_NOT_FOUND("Authentication Object not found", HttpStatus.FORBIDDEN.value()),
    STUDENT_MEMBER_INVALID("Member Invalid", HttpStatus.UNAUTHORIZED.value()),

    // Email Authentication
    EMAIL_FORMAT_INVALID("Email Format Invalid", HttpStatus.UNAUTHORIZED.value()),
    VERIFICATION_INVALID("Verification Invalid", HttpStatus.UNAUTHORIZED.value()),
    EMAIL_SEND_FAILED("Email Send Failed", HttpStatus.INTERNAL_SERVER_ERROR.value()),

    // Evidence
    EVIDENCE_NOT_FOUND("Evidence Not Found", HttpStatus.NOT_FOUND.value()),

    // Activity Evidence
    ACTIVITY_EVIDENCE_NOT_FOUNT("Activity Evidence Not Found", HttpStatus.NOT_FOUND.value()),
    DRAFT_ACTIVITY_EVIDENCE_NOT_FOUND("Draft Activity Not Found", HttpStatus.NOT_FOUND.value()),

    // Other Evidence
    OTHER_EVIDENCE_NOT_FOUND("Other Evidence Not Found", HttpStatus.NOT_FOUND.value()),

    // Reading Evidence
    READING_EVIDENCE_NOT_FOUND("Reading Evidence Not Found", HttpStatus.NOT_FOUND.value()),
    DRAFT_READING_EVIDENCE_NOT_FOUND("Draft Reading Not Found", HttpStatus.NOT_FOUND.value()),

    // Sheet
    SHEET_GENERATION_FAILED("Sheet Generation Failed", HttpStatus.INTERNAL_SERVER_ERROR.value());

    private final String message;
    private final int status;
}