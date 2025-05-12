package team.incude.gsmc.v2.domain.auth.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.incude.gsmc.v2.domain.auth.application.port.AuthApplicationPort;
import team.incude.gsmc.v2.domain.auth.presentation.data.request.*;
import team.incude.gsmc.v2.domain.auth.presentation.data.response.AuthTokenResponse;
import team.incude.gsmc.v2.domain.auth.presentation.data.request.ChangePasswordRequest;

@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
@RestController
public class AuthWebAdapter {

    private final AuthApplicationPort authApplicationPort;

    @PostMapping("/signin")
    public ResponseEntity<AuthTokenResponse> signIn(@Valid @RequestBody SignInRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(authApplicationPort.signIn(request.email(), request.password()));
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest request) {
        authApplicationPort.signUp(request.name(), request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/refresh")
    public ResponseEntity<AuthTokenResponse> refreshToken(@Valid @RequestBody RefreshRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(authApplicationPort.refresh(request.refreshToken()));
    }

    @PatchMapping("/verify-email")
    public ResponseEntity<Void> verifyEmail(@Valid @RequestBody VerificationCodeRequest request) {
        authApplicationPort.verifyEmail(request.code());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/send-email")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> sendAuthenticationEmail(@Valid @RequestBody SendEmailRequest request) {
        authApplicationPort.sendAuthenticationEmail(request.email());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        authApplicationPort.changePassword(request.email(), request.password());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
