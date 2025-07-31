package team.incube.gsmc.v2.global.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import team.incube.gsmc.v2.domain.evidence.application.port.RetryUploadZSetPort;
import team.incube.gsmc.v2.domain.evidence.presentation.data.FileUploadRetryMessageDto;
import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.adapter.Adapter;
import team.incube.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;

import java.time.Instant;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Redis Sorted Set을 활용하여 파일 업로드 재시도 메시지를 관리하는 어댑터입니다.
 * <p>재시도 메시지를 직렬화하여 Redis의 ZSet에 저장하고, 재시도 시간 기준으로
 * 메시지를 조회 및 삭제하는 기능을 제공합니다.
 * <p>{@link RetryUploadZSetPort} 인터페이스를 구현하며, 아웃바운드 포트 역할을 수행합니다.
 *
 * @author suuuuuuminnnnnn
 */
@Adapter(direction = PortDirection.OUTBOUND)
@Component
@Slf4j
@RequiredArgsConstructor
public class RetryUploadZSetAdapter implements RetryUploadZSetPort {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String RETRY_ZSET_KEY = "retry:file:upload:zset";

    /**
     * Redis ZSet에 재시도 메시지를 등록합니다.
     * @param message 재시도할 파일 업로드 메시지 DTO
     * @param retryTimeMillis 재시도 예정 시간(밀리초 단위)
     * @throws S3UploadFailedException 메시지 직렬화 실패 시 발생
     */
    @Override
    public void scheduleRetry(FileUploadRetryMessageDto message, long retryTimeMillis) {
        try {
            String serialized = objectMapper.writeValueAsString(message);
            redisTemplate.opsForZSet().add(RETRY_ZSET_KEY, serialized, retryTimeMillis);
            log.info("재시도 메시지 큐 등록 완료: {} (재시도 시간: {})", message.commandId(), Instant.ofEpochMilli(retryTimeMillis));
        } catch (JsonProcessingException e) {
            log.error("파일 재시도 메시지 직렬화 실패", e);
            throw new S3UploadFailedException();
        }
    }

    /**
     * 재시도 예정 시간까지 도달한 메시지를 조회합니다.
     * @param nowMillis 현재 시간(밀리초 단위)
     * @return 재시도 준비가 된 메시지 집합
     */
    @Override
    public Set<FileUploadRetryMessageDto> pollReadyToRetry(long nowMillis) {
        try {
            Set<String> serializedSet = redisTemplate.opsForZSet()
                    .rangeByScore(RETRY_ZSET_KEY, 0, nowMillis);

            if (serializedSet == null || serializedSet.isEmpty()) {
                return Collections.emptySet();
            }

            return serializedSet.stream()
                    .map(s -> {
                        try {
                            return objectMapper.readValue(s, FileUploadRetryMessageDto.class);
                        } catch (JsonProcessingException e) {
                            log.warn("재시도 메시지 역직렬화 실패, 해당 메시지 스킵: {}", s, e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            log.error("재시도 메시지 조회 중 오류 발생", e);
            return Collections.emptySet();
        }
    }

    /**
     * 재시도 큐에서 특정 메시지를 삭제합니다.
     * @param message 삭제할 재시도 메시지 DTO
     */
    @Override
    public void removeMessage(FileUploadRetryMessageDto message) {
        try {
            String serialized = objectMapper.writeValueAsString(message);
            redisTemplate.opsForZSet().remove(RETRY_ZSET_KEY, serialized);
            log.info("재시도 메시지 삭제 완료: {}", message.commandId());
        } catch (JsonProcessingException e) {
            log.warn("재시도 메시지 삭제 중 직렬화 실패: {}", message.commandId(), e);
        }
    }
}
