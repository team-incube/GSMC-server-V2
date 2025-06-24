package team.incude.gsmc.v2.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import team.incude.gsmc.v2.domain.evidence.application.usecase.service.RetryFileUploadService;
import team.incude.gsmc.v2.domain.evidence.domain.RetryUploadCommand;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class RetryFileUploadServiceTest {

    @Autowired
    private RetryFileUploadService retryFileUploadService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String RETRY_ZSET_KEY = "retry:file:upload:zset";

    @Test
    void 파일을_임시파일로_복사하고_ZSet에_등록한다() {
        // given
        RetryUploadCommand command = RetryUploadCommand.builder()
                .commandId(UUID.randomUUID().toString())
                .evidenceId(123L)
                .fileName("mock.txt")
                .evidenceType(EvidenceType.MAJOR)
                .email("tester@gsm.hs.kr")
                .build();

        byte[] mockData = "hello world".getBytes();
        InputStream inputStream = new ByteArrayInputStream(mockData);
        long now = System.currentTimeMillis();
        long delayMillis = 2000L;

        // when
        retryFileUploadService.execute(command, inputStream, delayMillis);

        // then
        Set<String> queuedMessages = redisTemplate.opsForZSet()
                .rangeByScore(RETRY_ZSET_KEY, now, now + delayMillis + 1000);

        assertThat(queuedMessages).isNotEmpty();
    }
}