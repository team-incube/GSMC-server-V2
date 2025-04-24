package team.incude.gsmc.v2.domain.sheet.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class GenerationSheetFailedException extends GsmcException {
    public GenerationSheetFailedException() {
        super(ErrorCode.SHEET_GENERATION_FAILED);
    }
}