package team.incude.gsmc.v2.domain.auth.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.auth.application.port.AuthenticationPersistencePort;
import team.incude.gsmc.v2.domain.auth.application.usecase.SignUpUseCase;
import team.incude.gsmc.v2.domain.auth.domain.Authentication;
import team.incude.gsmc.v2.domain.auth.exception.EmailFormatInvalidException;
import team.incude.gsmc.v2.domain.auth.exception.MemberExistException;
import team.incude.gsmc.v2.domain.auth.exception.MemberForbiddenException;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.member.domain.StudentDetail;
import team.incude.gsmc.v2.domain.member.domain.constant.MemberRole;


/**
 * 회원가입 유스케이스를 구현한 서비스 클래스입니다.
 * <p>사용자 회원가입을 진행하며, 인증 및 회원 중복 검증, 비밀번호 암호화, 학생 정보 연결 등의 작업을 수행합니다.
 * <p>회원가입 유스케이스는 {@code SignUpUseCase} 인터페이스를 구현합니다.
 * 이메일은 형식이 유효한지 확인되며, `s학번@gsm.hs.kr` 패턴을 만족해야 합니다.
 * 이메일 인증이 완료되지 않았거나 이미 가입된 회원인 경우 예외가 발생합니다.
 * 관련 예외:
 * <ul>
 *   <li>{@link MemberForbiddenException} - 이메일 인증 미완료</li>
 *   <li>{@link MemberExistException} - 이미 존재하는 회원</li>
 *   <li>{@link EmailFormatInvalidException} - 형식이 잘못된 이메일</li>
 * </ul>
 * <p>주요 기능:
 * <ul>
 *   <li>이메일 인증 여부 확인</li>
 *   <li>기존 회원 유무 확인</li>
 *   <li>이메일 형식 검증</li>
 *   <li>비밀번호 암호화</li>
 *   <li>학생 상세 정보 연결</li>
 * </ul>
 * @author jihoonwjj
 */
@Service
@Transactional
@RequiredArgsConstructor
public class SignUpService implements SignUpUseCase {

    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberPersistencePort memberPersistencePort;
    private final AuthenticationPersistencePort authenticationPersistencePort;
    private final StudentDetailPersistencePort studentDetailPersistencePort;

    /**
     * 회원 가입을 수행합니다.
     * <p>이메일 인증 여부를 확인한 후, 기존 회원 존재 여부와 이메일 형식을 검증합니다.
     * 비밀번호는 암호화되어 저장되며, 이후 해당 회원에 대한 학생 상세 정보와 연동됩니다.
     * @param name 회원 이름
     * @param email 회원 이메일 (학생 코드 포함)
     * @param password 암호화할 비밀번호
     * @throws MemberForbiddenException 이메일 인증 미완료 시
     * @throws MemberExistException 이미 가입된 회원일 경우
     * @throws EmailFormatInvalidException 이메일 형식이 유효하지 않을 경우
     */
    public void execute(String name, String email, String password) {
        Authentication authentication = authenticationPersistencePort.findAuthenticationByEmail(email);
        if (authentication == null || Boolean.FALSE.equals(authentication.getVerified())) {
            throw new MemberForbiddenException();
        }
        if (memberPersistencePort.existsMemberByEmail(email)) {
            throw new MemberExistException();
        }

        validateEmail(email);
        String studentCode = parseEmail(email);
        StudentDetail existedStudentDetail = studentDetailPersistencePort.findStudentDetailByStudentCodeWithLock(studentCode);

        Member member = Member.builder()
                .email(email)
                .name(name)
                .password(passwordEncoder.encode(password))
                .role(MemberRole.ROLE_STUDENT)
                .build();

        Member savedMember = memberPersistencePort.saveMember(member);

        StudentDetail studentDetail = StudentDetail.builder()
                .id(existedStudentDetail.getId())
                .member(savedMember)
                .grade(existedStudentDetail.getGrade())
                .classNumber(existedStudentDetail.getClassNumber())
                .number(existedStudentDetail.getNumber())
                .totalScore(existedStudentDetail.getTotalScore())
                .studentCode(existedStudentDetail.getStudentCode())
                .build();

        studentDetailPersistencePort.saveStudentDetail(studentDetail);
    }

    private void validateEmail(String email) {
        if (!email.startsWith("s") || !email.endsWith("@gsm.hs.kr")) {
            throw new EmailFormatInvalidException();
        }
    }
    
    /**
     * 이메일 주소에서 학생 코드를 추출합니다.
     * <p>형식은 반드시 "s학번@gsm.hs.kr"이어야 하며, 접두사 's' 제거 후 학번만 추출됩니다.
     * @param email 입력된 이메일 주소
     * @return 학번 문자열
     * @throws EmailFormatInvalidException 형식이 올바르지 않은 경우
     */
    private String parseEmail(String email) {
        if (!email.startsWith("s") || !email.endsWith("@gsm.hs.kr")) {
            throw new EmailFormatInvalidException();
        }
        return email.substring(1, email.indexOf("@"));
    }
}