package team.incube.gsmc.v2.domain.certificate.persentation.data;


/**
 * 자격증 정보를 표현하기 위한 내부 DTO입니다. 주로 서비스 또는 컨트롤러 계층에서 자격증 데이터를 가공하거나 전달할 때 사용되며,
 * 다음 필드들을 포함합니다:
 * <ul>
 *   <li>{@code id} - 자격증 고유 식별자</li>
 *   <li>{@code name} - 자격증 이름</li>
 *   <li>{@code acquisitionDate} - 자격증 취득일 (yyyy-MM-dd 형식)</li>
 *   <li>{@code evidenceUri} - 자격증 이미지 또는 파일의 URI</li>
 * </ul>
 * 이 DTO는 직접적으로 API 응답에 사용되기보다는 응답 객체 구성을 위한 중간 데이터로 활용됩니다.
 *
 * @author snowykte0426
 */
public record GetCertificateDto(
        Long id,
        String name,
        String acquisitionDate,
        String evidenceUri
) {
}