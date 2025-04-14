package team.incude.gsmc.v2.domain.auth.presentation.data.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VerificationCodeRequest(
        @NotBlank @Size(min=8, max=8) String code
) {
}
