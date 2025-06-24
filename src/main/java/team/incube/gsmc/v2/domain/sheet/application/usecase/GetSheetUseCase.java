package team.incube.gsmc.v2.domain.sheet.application.usecase;

import org.springframework.web.multipart.MultipartFile;

/**
 * 학년/반별 시트 파일을 조회하는 유스케이스를 정의하는 인터페이스입니다.
 * <p>입력받은 학년과 반 정보를 기반으로 해당 학급에 대한 시트 파일을 생성하거나 조회하여 반환합니다.
 * 반환 형식은 {@link MultipartFile}로, 주로 컨트롤러를 통해 파일 다운로드 형태로 전달됩니다.
 * 이 유스케이스는 어댑터 계층에서 호출되며, 시트 생성 로직은 실제 구현 클래스에서 수행됩니다.
 * @author snowykte0426
 */
public interface GetSheetUseCase {
    MultipartFile execute(Integer grade, Integer classNumber);
}