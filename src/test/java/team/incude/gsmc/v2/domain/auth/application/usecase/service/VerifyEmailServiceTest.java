package team.incude.gsmc.v2.domain.auth.application.usecase.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import team.incude.gsmc.v2.domain.auth.application.port.AuthCodePersistencePort;
import team.incude.gsmc.v2.domain.auth.exception.VerificationInvalidException;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VerifyEmailServiceTest {

    @InjectMocks
    private VerifyEmailService verifyEmailService;

    @Mock
    private AuthCodePersistencePort authCodePersistencePort;

    public VerifyEmailServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void it_verify_email_failed_invalid_code() {

        String invalidCode = "invalidCode";

        when(authCodePersistencePort.existsAuthCodeByCode(invalidCode)).thenReturn(false);

        assertThrows(VerificationInvalidException.class,
                () -> verifyEmailService.execute(invalidCode));
    }
}
