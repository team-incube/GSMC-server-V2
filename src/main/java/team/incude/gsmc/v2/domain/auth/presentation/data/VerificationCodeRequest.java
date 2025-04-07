package team.incude.gsmc.v2.domain.auth.presentation.data;

import jakarta.validation.constraints.Size;

public record VerificationCodeRequest(
        @Size(min=8, max=8) String code
) {
}
