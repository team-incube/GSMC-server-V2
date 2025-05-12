package team.incude.gsmc.v2.global.annotation.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import team.incude.gsmc.v2.global.validator.NotEmptyFileValidator;

import java.lang.annotation.*;

/**
 * 이 어노테이션은 {@link org.springframework.web.multipart.MultipartFile}이 비어있지 않은지 검증하는 데 사용됩니다.
 * <p>파일이 null이 아니고 비어있지 않은 경우에만 유효한 것으로 간주합니다.
 * <p>예를 들어, 자격증 등록 요청에서 파일이 비어있지 않은지 검증할 때 사용됩니다.
 * @see NotEmptyFileValidator
 * @author snowykte0426
 */
@Documented
@Constraint(validatedBy = NotEmptyFileValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyFile {
    String message() default "File must not be empty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}