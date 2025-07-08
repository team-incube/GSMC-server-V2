package team.incube.gsmc.v2.domain.sheet.application.usecase;

import org.springframework.web.multipart.MultipartFile;

/**
 * 모든 학급에 대한 시트 파일을 조회하는 유스케이스를 정의하는 인터페이스입니다.
 * <p>이 유스케이스는 모든 학급의 시트를 하나의 파일로 묶어 반환합니다.
 * 반환 형식은 {@link MultipartFile}로, 주로 컨트롤러를 통해 파일 다운로드 형태로 전달됩니다.
 * 이 유스케이스는 어댑터 계층에서 호출되며, 실제 구현 클래스에서 시트 생성 로직을 수행합니다.
 * @author snowykte0426
 */
public interface GetAllSheetUseCase {
    MultipartFile execute();
}