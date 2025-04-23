package team.incude.gsmc.v2.domain.sheet.application;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.sheet.application.port.SheetApplicationPort;
import team.incude.gsmc.v2.domain.sheet.application.usecase.GetSheetUseCase;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

@Adapter(direction = PortDirection.INBOUND)
@RequiredArgsConstructor
public class SheetApplicationAdapter implements SheetApplicationPort {

    private final GetSheetUseCase getSheetUseCase;

    @Override
    public MultipartFile getSheet(Integer grade, Integer classNumber) {
        return getSheetUseCase.execute(grade, classNumber);
    }
}