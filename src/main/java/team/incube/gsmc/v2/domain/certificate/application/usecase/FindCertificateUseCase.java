package team.incube.gsmc.v2.domain.certificate.application.usecase;

import team.incube.gsmc.v2.domain.certificate.persentation.data.response.GetCertificateResponse;

/**
 * 자격증 조회 기능을 제공하는 인터페이스입니다.
 * <p>현재 로그인한 사용자 또는 특정 학생의 자격증 목록을 조회하는 메서드를 정의합니다.
 * <p>주요 기능:
 * <ul>
 *   <li>현재 사용자의 이메일을 기준으로 학생 코드를 조회하고 자격증 목록 반환</li>
 *   <li>학생 코드를 직접 받아 해당 자격증 목록 반환</li>
 * </ul>
 * <p>각 자격증은 {@link GetCertificateResponse}에 담겨 반환됩니다.
 * @author snowykte0426
 */
public interface FindCertificateUseCase {
    GetCertificateResponse execute();

    GetCertificateResponse execute(String studentCode);
}