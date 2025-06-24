package team.incube.gsmc.v2.domain.certificate.persistence.projection;

import java.time.LocalDate;

/**
 * 자격증 정보를 부분적으로 조회하기 위한 Projection 클래스입니다.
 * <p>QueryDSL을 사용할 때 필요한 필드만 매핑하여 성능 최적화에 활용됩니다.
 * <p>포함 필드:
 * <ul>
 *   <li>{@code id} - 자격증 고유 식별자</li>
 *   <li>{@code name} - 자격증 이름</li>
 *   <li>{@code acquisitionDate} - 자격증 취득일</li>
 *   <li>{@code fileUri} - 자격증 파일 URI</li>
 * </ul>
 * 이 projection은 전체 엔티티를 로드하지 않고 필요한 값만 가져오기 위한 용도로 사용됩니다.
 * @author snowykte0426
 */
public record CertificateProjection(
        Long id,
        String name,
        LocalDate acquisitionDate,
        String fileUri
) {
}