package ai.omnicure.iam.application.service;

import ai.omnicure.iam.application.feature.auth.dto.TokenResponseDto;
import ai.omnicure.iam.domain.entity.User;

public interface ITokenService {

    TokenResponseDto generateTokens(User user);

    String generateRefreshToken();
}
