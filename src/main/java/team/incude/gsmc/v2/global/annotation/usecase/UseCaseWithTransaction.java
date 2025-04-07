package team.incude.gsmc.v2.global.annotation.usecase;

import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@UseCase
@Transactional(rollbackFor = Exception.class)
public @interface UseCaseWithTransaction {
}
