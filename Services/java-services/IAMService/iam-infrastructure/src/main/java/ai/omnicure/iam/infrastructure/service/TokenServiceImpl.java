package ai.omnicure.iam.infrastructure.service;

import ai.omnicure.core.shared.constant.KeyConstants;
import ai.omnicure.iam.application.feature.auth.dto.TokenResponseDto;
import ai.omnicure.iam.application.service.ITokenService;
import ai.omnicure.iam.domain.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import ai.omnicure.iam.infrastructure.config.JwtSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import io.jsonwebtoken.io.Decoders;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements ITokenService {

    private final JwtSettings jwtSettings;

    @Override
    public TokenResponseDto generateTokens(User user) {
        LocalDateTime accessTokenExpiry = LocalDateTime.now().plusMinutes(jwtSettings.getAccessTokenDurationMinutes());
        LocalDateTime refreshTokenExpiry = LocalDateTime.now().plusDays(jwtSettings.getRefreshTokenDurationDays());

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSettings.getKey()));

        Date expiration = Date.from(accessTokenExpiry.atZone(ZoneId.systemDefault()).toInstant());

        String tokenId = UUID.randomUUID().toString();

        String accessToken = Jwts.builder()
                .subject(user.getUserId().toString())
                .id(tokenId)
                .issuer(jwtSettings.getIssuer())
                .audience().add(jwtSettings.getAudience()).and()
                .claim("email", user.getEmail())
                .claim("name", user.getUserName())
                .issuedAt(new Date())
                .expiration(expiration)
                .signWith(key)
                .compact();

        String refreshToken = generateRefreshToken();

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .tokenId(tokenId)
                .accessTokenExpiry(accessTokenExpiry)
                .refreshToken(refreshToken)
                .refreshTokenExpiry(refreshTokenExpiry)
                .build();
    }

    @Override
    public String generateRefreshToken() {
        byte[] randomBytes = new byte[64];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getEncoder().encodeToString(randomBytes);
    }
}
