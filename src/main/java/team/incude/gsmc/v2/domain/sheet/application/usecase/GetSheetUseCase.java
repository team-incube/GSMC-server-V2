package team.incude.gsmc.v2.domain.sheet.application.usecase;

import org.springframework.web.multipart.MultipartFile;

public interface GetSheetUseCase {
    MultipartFile execute(Integer grade, Integer classNumber);
}