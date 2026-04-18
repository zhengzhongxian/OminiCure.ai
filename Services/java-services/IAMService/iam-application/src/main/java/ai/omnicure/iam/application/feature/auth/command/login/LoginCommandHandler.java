package ai.omnicure.iam.application.feature.auth.command.login;

import ai.omnicure.core.application.cache.ICacheService;
import ai.omnicure.core.application.cqrs.Command;
import ai.omnicure.core.domain.exception.DomainException;
import ai.omnicure.core.shared.constant.message.business.AuthMessage;
import ai.omnicure.iam.application.feature.auth.cache.RefreshTokenCacheEntry;
import ai.omnicure.iam.application.feature.auth.dto.RefreshTokenCacheDto;
import ai.omnicure.iam.application.feature.auth.dto.TokenResponseDto;
import ai.omnicure.iam.application.repository.IUserWriteRepository;
import ai.omnicure.iam.application.service.ICryptographyService;
import ai.omnicure.iam.application.service.ITokenService;
import ai.omnicure.iam.domain.entity.User;
import ai.omnicure.iam.domain.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginCommandHandler implements Command.CommandHandler<LoginCommand, TokenResponseDto> {

    private final IUserWriteRepository userRepository;
    private final ITokenService tokenService;
    private final ICryptographyService cryptographyService;
    private final ICacheService cacheService;
    private final RefreshTokenCacheEntry refreshTokenCacheEntry;

    @Override
    public TokenResponseDto handle(LoginCommand command) {
        User user = userRepository.findByUserNameOrEmail(command.getUsernameOrEmail())
                .orElseThrow(() -> new DomainException(AuthMessage.Login.FAILED, 401));

        if (!cryptographyService.verifyPassword(command.getPassword(), user.getPassword())) {
            throw new DomainException(AuthMessage.Login.FAILED, 401);
        }

        if (!Boolean.TRUE.equals(user.getIsVerified())) {
            throw new DomainException(AuthMessage.Login.UNVERIFIED, 401);
        }

        if (user.getStatus() == UserStatus.BLOCKED) {
            throw new DomainException(AuthMessage.Login.BANNED, 403);
        }

        TokenResponseDto tokens = tokenService.generateTokens(user);

        RefreshTokenCacheDto cacheDto = RefreshTokenCacheDto.builder()
                .userId(user.getUserId())
                .deviceId(command.getDeviceName())
                .isRevoked(false)
                .tokenId(tokens.getTokenId())
                .parentTokenId(null)
                .build();

        String redisKey = refreshTokenCacheEntry.getKey(tokens.getRefreshToken());
        Duration ttl = Duration.between(LocalDateTime.now(), tokens.getRefreshTokenExpiry());
        cacheService.hashSet(redisKey, cacheDto.toHashMap(), ttl);

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.update(user);

        log.info("User [{}] logged in successfully", user.getUserName());

        return tokens;
    }
}
