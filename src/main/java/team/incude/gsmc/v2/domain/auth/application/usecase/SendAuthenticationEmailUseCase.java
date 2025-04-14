package team.incude.gsmc.v2.domain.auth.application.usecase;

import jakarta.mail.MessagingException;

public interface SendAuthenticationEmailUseCase {

    void execute(String email) throws MessagingException;
}
