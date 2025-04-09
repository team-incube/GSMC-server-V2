package team.incude.gsmc.v2.domain.auth.presentation.data.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @Email @Size(max=16) String email,
        @NotBlank @Size(max=30) String password
) {
}
