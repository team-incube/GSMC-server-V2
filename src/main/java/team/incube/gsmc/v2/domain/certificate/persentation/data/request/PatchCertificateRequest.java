package team.incube.gsmc.v2.domain.certificate.persentation.data.request;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

/**
 * 자격증 정보를 수정하기 위한 요청 데이터를 담는 DTO입니다. 클라이언트가 자격증 정보를 수정할 때 사용하는 형식으로,
 * 다음 필드들을 포함합니다:
 * <ul>
 *   <li>{@code name} - 변경할 자격증 이름 (선택)</li>
 *   <li>{@code acquisitionDate} - 변경할 자격증 취득일 (선택)</li>
 *   <li>{@code file} - 새로운 자격증 이미지 또는 PDF 파일 (선택)</li>
 * </ul>
 * 모든 필드는 선택 사항이며, 클라이언트는 수정이 필요한 필드만 포함하여 요청할 수 있습니다.
 * @author snowykte0426
 */
public record PatchCertificateRequest(
        String name,
        LocalDate acquisitionDate,
        MultipartFile file
) {
}