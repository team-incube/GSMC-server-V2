package team.incude.gsmc.v2.domain.auth.presentation.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RefreshRequest (
        @NotBlank @Size(max=256) String refreshToken
){
}
