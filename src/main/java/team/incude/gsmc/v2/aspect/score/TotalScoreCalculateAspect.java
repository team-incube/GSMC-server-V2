package team.incude.gsmc.v2.aspect.score;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.score.application.port.ScoreApplicationPort;
import team.incude.gsmc.v2.global.annotation.aspect.CalculateTotalScoreStduentCode;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Component
@RequiredArgsConstructor
public class TotalScoreCalculateAspect {

    private static final long FIXED_DELAY_MS = 5_000;

    private final TaskScheduler taskScheduler;
    private final ScoreApplicationPort scoreApplicationPort;

    @AfterReturning(pointcut = "@annotation(team.incude.gsmc.v2.global.annotation.aspect.TriggerCalculateTotalScore)")
    public void onSuccessSchedule(JoinPoint jp) {
        MethodSignature sig = (MethodSignature) jp.getSignature();
        Method method = sig.getMethod();
        Annotation[][] paramAnns = method.getParameterAnnotations();
        Object[] args = jp.getArgs();
        String studentCode = null;
        for (int i = 0; i < paramAnns.length; i++) {
            for (Annotation ann : paramAnns[i]) {
                if (ann instanceof CalculateTotalScoreStduentCode) {
                    studentCode = (String) args[i];
                    break;
                }
            }
            if (studentCode != null) break;
        }
        if (studentCode == null) {
            throw new IllegalStateException(
                    "@TriggerCalculateTotalScore가 붙은 메소드에 @CalculateTotalScoreEmail 붙은 String 파라미터가 필요합니다."
            );
        }
        Date runAt = new Date(System.currentTimeMillis() + FIXED_DELAY_MS);
        final String finalStudentCode = studentCode;
        taskScheduler.schedule(() -> scoreApplicationPort.calculateTotalScore(finalStudentCode), runAt);
    }
}