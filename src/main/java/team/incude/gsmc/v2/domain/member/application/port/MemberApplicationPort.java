package team.incude.gsmc.v2.domain.member.application.port;

import team.incude.gsmc.v2.domain.member.presentation.data.response.GetStudentResponse;
import team.incude.gsmc.v2.domain.member.presentation.data.response.SearchStudentResponse;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.List;

/**
 * Member 도메인의 주요 유스케이스를 정의하는 애플리케이션 포트 인터페이스입니다.
 * <p>학생 목록 조회, 검색, 현재 로그인한 학생 조회, 특정 학생 코드 기반 조회 등의 기능을 정의하며,
 * Web 어댑터 계층이 이 포트를 통해 도메인 로직에 접근합니다.
 * <p>{@code @Port(direction = PortDirection.INBOUND)}로 선언되어 외부 요청이 도메인 계층으로 유입되는 진입점 역할을 합니다.
 * <ul>
 *   <li>{@code findAllStudents()} - 전체 학생 목록 조회</li>
 *   <li>{@code searchStudents(...)} - 이름/학년/반 조건으로 학생 검색</li>
 *   <li>{@code findCurrentStudent()} - 현재 로그인한 사용자 정보 조회</li>
 *   <li>{@code findMemberByStudentCode(...)} - 학생 코드 기반 조회</li>
 * </ul>
 * @author snowykte0426
 */
@Port(direction = PortDirection.INBOUND)
public interface MemberApplicationPort {
    List<GetStudentResponse> findAllStudents();

    SearchStudentResponse searchStudents(String name, Integer grade, Integer classNumber, Integer page, Integer size);

    GetStudentResponse findCurrentStudent();

    GetStudentResponse findMemberByEmail(String email);
}