package team.incude.gsmc.v2.global.redis.mapper;

import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.evidence.domain.RetryUploadCommand;
import team.incude.gsmc.v2.domain.evidence.presentation.data.FileUploadRetryMessageDto;

@Component
public class RetryUploadMapper {

    public FileUploadRetryMessageDto toMessage(RetryUploadCommand command) {
        return FileUploadRetryMessageDto.builder()
                .commandId(command.getCommandId())
                .evidenceId(command.getEvidenceId())
                .fileName(command.getFileName())
                .tempFilePath(command.getTempFilePath())
                .evidenceType(command.getEvidenceType())
                .email(command.getEmail())
                .build();
    }

    public RetryUploadCommand toDomain(FileUploadRetryMessageDto message) {
        return RetryUploadCommand.builder()
                .commandId(message.commandId())
                .evidenceId(message.evidenceId())
                .fileName(message.fileName())
                .tempFilePath(message.tempFilePath())
                .evidenceType(message.evidenceType())
                .email(message.email())
                .build();
    }
}