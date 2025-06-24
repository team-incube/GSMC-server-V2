package team.incube.gsmc.v2.domain.certificate.persentation.data.response;

import team.incube.gsmc.v2.domain.certificate.persentation.data.GetCertificateDto;

import java.util.List;

/**
 * 자격증 목록을 반환하는 응답 DTO입니다. 클라이언트에게 여러 개의 자격증 정보를 전달할 때 사용되며,
 * 각 자격증은 {@code GetCertificateDto} 객체로 표현됩니다.
 * <p>주로 자격증 목록 조회 API의 응답 본문에 사용됩니다.
 * @param certificates 자격증 정보를 담은 DTO 리스트
 * @author snowykte0426
 */
public record GetCertificateResponse(
        List<GetCertificateDto> certificates
) {
}