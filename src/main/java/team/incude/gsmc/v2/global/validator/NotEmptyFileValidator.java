package team.incude.gsmc.v2.global.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.global.annotation.validator.NotEmptyFile;

/**
 * 해당 클래스는 {@link NotEmptyFile} 어노테이션을 사용하여 MultipartFile이 비어있지 않은지 검증하는 Validator입니다.
 * <p>MultipartFile이 null이 아니고 비어있지 않은 경우에만 유효한 것으로 간주합니다.
 * <p>이 Validator는 {@link ConstraintValidator} 인터페이스를 구현하며, {@link NotEmptyFile} 어노테이션과 함께 사용됩니다.
 * <p>예를 들어, 자격증 등록 요청에서 파일이 비어있지 않은지 검증할 때 사용됩니다.
 * @see NotEmptyFile
 * @see MultipartFile
 * @see ConstraintValidator
 * @author snowykte0426
 */
public class NotEmptyFileValidator implements ConstraintValidator<NotEmptyFile, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        return file != null && !file.isEmpty();
    }
}