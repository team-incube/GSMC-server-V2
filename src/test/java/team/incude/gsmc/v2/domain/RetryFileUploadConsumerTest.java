package team.incude.gsmc.v2.domain;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import team.incude.gsmc.v2.domain.evidence.application.consumer.RetryFileUploadConsumer;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidenceApplicationPort;
import team.incude.gsmc.v2.domain.evidence.application.port.RetryUploadZSetPort;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.presentation.data.FileUploadRetryMessageDto;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class RetryFileUploadConsumerTest {

    @Mock
    private RetryUploadZSetPort retryUploadZSetPort;

    @Mock
    private EvidenceApplicationPort evidenceApplicationPort;

    @InjectMocks
    private RetryFileUploadConsumer retryFileUploadConsumer;

    @Test
    void 임시파일이_존재하면_업로드하고_큐에서_제거한다() throws Exception {
        // given
        String tempFilePath = File.createTempFile("test-", ".txt").getAbsolutePath();
        try (FileOutputStream fos = new FileOutputStream(tempFilePath)) {
            fos.write("mock-data".getBytes());
        }

        FileUploadRetryMessageDto dto = new FileUploadRetryMessageDto(
                "cmd-id",
                1L,
                "mock.txt",
                tempFilePath,
                EvidenceType.MAJOR,
                "test@gsm.hs.kr"
        );

        when(retryUploadZSetPort.pollReadyToRetry(anyLong()))
                .thenReturn(Set.of(dto));

        RetryFileUploadConsumer consumer = new RetryFileUploadConsumer(retryUploadZSetPort, evidenceApplicationPort);

        // when
        consumer.consume();

        // then
        verify(evidenceApplicationPort, times(1))
                .updateEvidenceFile(eq(1L), eq("mock.txt"), any(), eq(EvidenceType.MAJOR), eq("test@gsm.hs.kr"));

        verify(retryUploadZSetPort, times(1)).removeMessage(eq(dto));
    }
}
