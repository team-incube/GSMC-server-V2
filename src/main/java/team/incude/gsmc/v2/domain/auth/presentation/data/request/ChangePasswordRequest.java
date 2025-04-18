package team.incude.gsmc.v2.domain.auth.presentation.data.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @Email @NotBlank @Size(max=16) String email,
        @NotBlank @Size(min=8, max=20) String password
) {
}
