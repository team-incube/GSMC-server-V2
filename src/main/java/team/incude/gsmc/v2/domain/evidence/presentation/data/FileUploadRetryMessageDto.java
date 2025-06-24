package team.incude.gsmc.v2.domain.evidence.presentation.data;

import lombok.Builder;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;

/**
 * 파일 업로드 재시도 메시지 DTO
 * 재시도 큐에 저장 및 처리되는 재시도 메시지의 데이터를 담습니다.
 * author suuuuuuminnnnnn
 */
@Builder
public record FileUploadRetryMessageDto(
        String commandId,
        Long evidenceId,
        String fileName,
        String tempFilePath,
        EvidenceType evidenceType,
        String email
) {}
