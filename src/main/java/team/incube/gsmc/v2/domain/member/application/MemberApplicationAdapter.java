package team.incube.gsmc.v2.domain.member.application;

import lombok.RequiredArgsConstructor;
import team.incube.gsmc.v2.domain.member.application.port.MemberApplicationPort;
import team.incube.gsmc.v2.domain.member.application.usecase.FindAllStudentUseCase;
import team.incube.gsmc.v2.domain.member.application.usecase.FindCurrentStudentUseCase;
import team.incube.gsmc.v2.domain.member.application.usecase.FindStudentByEmailUseCase;
import team.incube.gsmc.v2.domain.member.application.usecase.SearchStudentUseCase;
import team.incube.gsmc.v2.domain.member.presentation.data.response.GetStudentResponse;
import team.incube.gsmc.v2.domain.member.presentation.data.response.SearchStudentResponse;
import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;

/**
 * 사용자 관련 유스케이스의 진입점을 정의하는 애플리케이션 어댑터 클래스입니다.
 * <p>{@link MemberApplicationPort}를 구현하며, Web 계층의 요청을 실제 유스케이스 실행으로 위임합니다.
 * 내부적으로 네 가지 주요 유스케이스를 조합하여 사용자 조회 관련 기능을 제공합니다.
 * <ul>
 *   <li>{@link FindAllStudentUseCase} - 전체 학생 목록 조회</li>
 *   <li>{@link SearchStudentUseCase} - 이름/학년/반 조건 기반 검색</li>
 *   <li>{@link FindCurrentStudentUseCase} - 현재 로그인한 사용자 정보 조회</li>
 *   <li>{@link FindStudentByEmailUseCase} - 학생 코드 기반 단일 조회</li>
 * </ul>
 * 이 클래스는 어댑터 계층에서 도메인 계층으로의 연결을 담당하는 구조적 진입점입니다.
 * @author snowykte0426
 */
@Adapter(direction = PortDirection.INBOUND)
@RequiredArgsConstructor
public class MemberApplicationAdapter implements MemberApplicationPort {

    private final FindAllStudentUseCase findAllStudentUseCase;
    private final SearchStudentUseCase searchStudentUseCase;
    private final FindCurrentStudentUseCase findCurrentStudentUseCase;
    private final FindStudentByEmailUseCase findStudentByEmailUseCase;

    @Override
    public List<GetStudentResponse> findAllStudents() {
        return findAllStudentUseCase.execute();
    }

    @Override
    public SearchStudentResponse searchStudents(String name, Integer grade, Integer classNumber, Integer page, Integer size) {
        return searchStudentUseCase.execute(name, grade, classNumber, page, size);
    }

    @Override
    public GetStudentResponse findCurrentStudent() {
        return findCurrentStudentUseCase.execute();
    }

    @Override
    public GetStudentResponse findMemberByEmail(String email) {
        return findStudentByEmailUseCase.execute(email);
    }
}