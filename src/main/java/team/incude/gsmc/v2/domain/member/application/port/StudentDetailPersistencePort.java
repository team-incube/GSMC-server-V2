package team.incude.gsmc.v2.domain.member.application.port;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team.incude.gsmc.v2.domain.member.domain.StudentDetail;
import team.incude.gsmc.v2.domain.member.domain.StudentDetailWithEvidence;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.List;

/**
 * 학생 상세 정보(StudentDetail)의 영속성 계층과 통신하는 포트 인터페이스입니다.
 * <p>학생 정보 및 증빙 정보를 기반으로 한 복합 조회, 점수 집계, 저장 등의 기능을 제공합니다.
 * 이 포트는 도메인 계층이 데이터 저장소에 직접 의존하지 않도록 분리하기 위해 사용됩니다.
 * <p>{@code @Port(direction = PortDirection.OUTBOUND)}로 선언되며,
 * 실제 구현은 JPA, QueryDSL, 외부 API 연동 등 다양한 방식으로 확장될 수 있습니다.
 * 주요 기능:
 * <ul>
 *   <li>학생 코드 또는 이메일 기반 조회 (락 포함)</li>
 *   <li>사용자 정보가 존재하는 학년/반 조건 학생 목록 조회</li>
 *   <li>증빙자료 포함 조회</li>
 *   <li>검토 상태 존재 여부 기반 목록 필터링</li>
 *   <li>검색 조건 및 페이징 처리된 학생 조회</li>
 *   <li>총합 점수 조회 및 저장</li>
 * </ul>
 * @author snowykte0426
 */
@Port(direction = PortDirection.OUTBOUND)
public interface StudentDetailPersistencePort {
    StudentDetail findStudentDetailByEmail(String email);

    StudentDetail findStudentDetailByEmailWithLock(String email);

    StudentDetail findStudentDetailByMemberEmail(String email);

    List<StudentDetail> findStudentDetailByGradeAndClassNumberAndMemberNotNull(Integer grade, Integer classNumber);

    StudentDetailWithEvidence findStudentDetailWithEvidenceByEmail(String email);

    StudentDetailWithEvidence findStudentDetailWithEvidenceByMemberEmail(String email);

    List<StudentDetailWithEvidence> findStudentDetailWithEvidenceReviewStatusNotNullMember();

    Page<StudentDetailWithEvidence> searchStudentDetailWithEvidenceReviewStatusNotNullMember(String name, Integer grade, Integer classNumber, Pageable pageable);

    Integer findTotalScoreByEmail(String email);

    StudentDetail saveStudentDetail(StudentDetail studentDetail);
}