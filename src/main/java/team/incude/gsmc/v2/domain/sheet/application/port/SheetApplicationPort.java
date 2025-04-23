package team.incude.gsmc.v2.domain.sheet.application.port;

import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

@Port(direction = PortDirection.INBOUND)
public interface SheetApplicationPort {
    MultipartFile getSheet(Integer grade, Integer classNumber);
}