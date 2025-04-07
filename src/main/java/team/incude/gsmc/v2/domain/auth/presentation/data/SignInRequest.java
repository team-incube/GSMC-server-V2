package team.incude.gsmc.v2.domain.auth.presentation.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignInRequest(
        @Email @Size(max=16) String email,
        @NotBlank @Size(max=30) String password
) {
}
