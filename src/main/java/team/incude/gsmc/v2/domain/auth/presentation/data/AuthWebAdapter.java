package team.incude.gsmc.v2.domain.auth.presentation.data;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.incude.gsmc.v2.domain.auth.application.port.AuthApplicationPort;
import team.incude.gsmc.v2.domain.auth.presentation.data.request.*;
import team.incude.gsmc.v2.domain.auth.presentation.data.response.AuthTokenResponse;

@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
@RestController
public class AuthWebAdapter {

    private final AuthApplicationPort authApplicationPort;

    @PostMapping("/signin")
    public ResponseEntity<AuthTokenResponse> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authApplicationPort.signIn(signInRequest.email(), signInRequest.password()));
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        authApplicationPort.signUp(signUpRequest.email(), signUpRequest.password(), signUpRequest.name());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/refresh")
    public ResponseEntity<AuthTokenResponse> refreshToken(@Valid @RequestBody RefreshRequest refreshRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authApplicationPort.refresh(refreshRequest.refreshToken()));
    }

    @PatchMapping("/verify-email")
    public ResponseEntity<Void> verifyEmail(@Valid @RequestBody VerificationCodeRequest verificationCodeRequest) {
        authApplicationPort.verifyEmail(verificationCodeRequest.code());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/send-email")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> sendAuthenticationEmail(@Valid @RequestBody SendEmailRequest sendEmailRequest) throws MessagingException {
        authApplicationPort.sendAuthenticationEmail(sendEmailRequest.email());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
