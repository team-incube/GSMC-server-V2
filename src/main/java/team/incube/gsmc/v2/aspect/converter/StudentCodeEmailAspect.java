package team.incube.gsmc.v2.aspect.converter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;

/**
 * "studentCode"라는 이름의 매개변수를 이메일 주소 형식으로 변환하는 Aspect입니다.
 * 변환된 값은 다음 형식입니다: s{studentCode}@gsm.hs.kr
 * 이 Aspect는 @RestController가 붙은 클래스 내의 모든 메서드에 적용됩니다.
 * @author suuuuuuminnnnnn
 */
@Aspect
@Component
public class StudentCodeEmailAspect {

    /**
     * &#064;RestController  어노테이션이 붙은 클래스 내의 메서드를 가로채어,
     * "studentCode"라는 이름의 인자가 있으면 이를 이메일 주소 형식으로 변환합니다.
     * @param joinPoint 가로채어진 메서드를 나타내는 조인 포인트
     * @return 변환된 인자를 사용하여 실행된 메서드의 결과
     * @throws Throwable 가로채어진 메서드 실행 중 발생할 수 있는 예외
     * @author suuuuuuminnnnnn
     */
    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object convertStudentCodeToEmail(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = signature.getMethod().getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getName().equals("studentCode") && args[i] instanceof String) {
                args[i] = "s" + args[i] + "@gsm.hs.kr";
            }
        }
        return joinPoint.proceed(args);
    }
}
