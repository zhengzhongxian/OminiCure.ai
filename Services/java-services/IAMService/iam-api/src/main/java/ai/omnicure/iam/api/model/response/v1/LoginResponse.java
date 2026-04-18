package ai.omnicure.iam.api.model.response.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private LocalDateTime accessTokenExpiry;
    private LocalDateTime refreshTokenExpiry;
}
