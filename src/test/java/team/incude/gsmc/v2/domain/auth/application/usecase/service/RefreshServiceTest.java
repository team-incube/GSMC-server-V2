package team.incude.gsmc.v2.domain.auth.application.usecase.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import team.incude.gsmc.v2.domain.auth.exception.RefreshTokenInvalidException;
import team.incude.gsmc.v2.global.security.jwt.usecase.JwtParserUseCase;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RefreshServiceTest {

    @InjectMocks
    private RefreshService refreshService;

    @Mock
    private JwtParserUseCase jwtParserUseCase;

    public RefreshServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void it_refresh_failed_invalid_token() {

        String invalidToken = "invalidToken";

        when(jwtParserUseCase.validateRefreshToken(invalidToken)).thenReturn(false);

        assertThrows(RefreshTokenInvalidException.class,
                () -> refreshService.execute(invalidToken));
    }
}
