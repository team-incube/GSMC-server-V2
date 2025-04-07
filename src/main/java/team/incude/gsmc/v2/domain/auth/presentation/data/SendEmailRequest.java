package team.incude.gsmc.v2.domain.auth.presentation.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record SendEmailRequest(
        @Email @Size(max = 16 , min = 16) String email
) {
}
