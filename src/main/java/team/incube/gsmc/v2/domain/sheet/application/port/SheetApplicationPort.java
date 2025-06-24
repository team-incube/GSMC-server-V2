package team.incube.gsmc.v2.domain.sheet.application.port;

import org.springframework.web.multipart.MultipartFile;
import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.port.Port;

/**
 * 학년/반별 시트 다운로드 유스케이스를 정의하는 포트 인터페이스입니다.
 * <p>어댑터(Web 계층 등)로부터 시트 다운로드 요청을 받아 처리하기 위한 진입점 역할을 합니다.
 * {@link #getSheet(Integer, Integer)} 메서드를 통해 특정 학년과 반에 해당하는 시트 파일을 제공합니다.
 * 이 포트는 {@code @Port(direction = PortDirection.INBOUND)}로 정의되어 있으며,
 * 실제 로직은 서비스 구현체에서 처리됩니다.
 * @author snowykte0426
 */
@Port(direction = PortDirection.INBOUND)
public interface SheetApplicationPort {
    MultipartFile getSheet(Integer grade, Integer classNumber);
}