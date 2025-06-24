package team.incube.gsmc.v2.domain.sheet.application;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import team.incube.gsmc.v2.domain.sheet.application.port.SheetApplicationPort;
import team.incube.gsmc.v2.domain.sheet.application.usecase.GetSheetUseCase;
import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.adapter.Adapter;

/**
 * 시트 다운로드 유스케이스의 진입점을 정의하는 어댑터 클래스입니다.
 * <p>{@link SheetApplicationPort}를 구현하며, Web 계층으로부터의 요청을 받아 {@link GetSheetUseCase}에 위임합니다.
 * <p>{@code @Adapter(direction = PortDirection.INBOUND)}로 선언되어 외부 요청을 내부 유스케이스로 연결하는 역할을 수행합니다.
 * @author snowykte0426
 */
@Adapter(direction = PortDirection.INBOUND)
@RequiredArgsConstructor
public class SheetApplicationAdapter implements SheetApplicationPort {

    private final GetSheetUseCase getSheetUseCase;

    /**
     * 특정 학년과 반에 해당하는 시트 파일을 조회합니다.
     * @param grade 학년
     * @param classNumber 반 번호
     * @return MultipartFile 형태의 시트 파일
     */
    @Override
    public MultipartFile getSheet(Integer grade, Integer classNumber) {
        return getSheetUseCase.execute(grade, classNumber);
    }
}