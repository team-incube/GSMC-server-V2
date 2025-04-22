package team.incude.gsmc.v2.domain.auth.application.usecase.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import team.incude.gsmc.v2.global.thirdparty.email.EmailSendService;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendAuthenticationEmailServiceTest {

    @InjectMocks
    private SendAuthenticationEmailService sendAuthenticationEmailService;

    @Mock
    private EmailSendService emailSendService;

    public SendAuthenticationEmailServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void it_email_authentication_success() {

        String email = "test@gsm.hs.kr";

        sendAuthenticationEmailService.execute(email);

        verify(emailSendService,
                times(1)).sendEmail(anyString(), anyString());
    }
}

