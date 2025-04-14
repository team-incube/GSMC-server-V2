package team.incude.gsmc.v2.domain.auth.application.usecase;

import team.incude.gsmc.v2.domain.auth.presentation.data.response.AuthTokenResponse;

public interface RefreshUseCase {

    AuthTokenResponse execute(String refreshToken);
}
