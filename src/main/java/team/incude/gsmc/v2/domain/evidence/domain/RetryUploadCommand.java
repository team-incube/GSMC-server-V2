package team.incude.gsmc.v2.domain.evidence.domain;

import lombok.Builder;
import lombok.Getter;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;

/**
 * 재시도 업로드 커맨드 객체
 * 재시도 큐에 들어가는 파일 업로드 명령을 캡슐화합니다.
 * author suuuuuuminnnnnn
 */
@Getter
@Builder
public class RetryUploadCommand {
    private final String commandId;
    private final Long evidenceId;
    private final String fileName;
    private final String tempFilePath;
    private final EvidenceType evidenceType;
    private final String email;
}
