package ai.omnicure.iam.application.feature.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseDto {
    private String accessToken;
    private String tokenId;
    private LocalDateTime accessTokenExpiry;
    private String refreshToken;
    private LocalDateTime refreshTokenExpiry;
}
