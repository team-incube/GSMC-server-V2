package team.incude.gsmc.v2.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Spring TaskScheduler 설정을 담당하는 구성 클래스입니다.
 * <p>{@link EnableScheduling}을 통해 스케줄링 기능을 활성화하고,
 * {@link ThreadPoolTaskScheduler}를 커스터마이징하여 비동기 작업 실행을 위한 스레드 풀을 구성합니다.
 * <p>설정된 스레드 풀은 스케줄 기반 작업(@Scheduled)에 사용되며,
 * graceful shutdown, prefix 설정 등을 통해 안정적인 운영을 지원합니다.
 * @author snowykte0426
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {

    private static final int THREAD_POOL_SIZE = 15;
    private static final String THREAD_NAME_PREFIX = "gsmc-scheduler-";

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(THREAD_POOL_SIZE);
        taskScheduler.setThreadNamePrefix(THREAD_NAME_PREFIX);
        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        taskScheduler.initialize();
        return taskScheduler;
    }
}