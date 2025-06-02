package team.incude.gsmc.v2.aspect.converter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;

@Aspect
@Component
public class StudentCodeEmailAspect {

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
