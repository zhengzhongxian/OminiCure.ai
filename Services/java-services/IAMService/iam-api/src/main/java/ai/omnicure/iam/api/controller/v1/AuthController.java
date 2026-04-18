package ai.omnicure.iam.api.controller.v1;

import ai.omnicure.core.shared.constant.message.business.AuthMessage;
import ai.omnicure.core.web.model.response.v1.ResultRes;
import ai.omnicure.iam.api.model.request.v1.LoginParams;
import ai.omnicure.iam.api.model.response.v1.LoginResponse;
import ai.omnicure.iam.application.feature.auth.command.login.LoginCommand;
import ai.omnicure.iam.application.feature.auth.dto.TokenResponseDto;
import an.awesome.pipelinr.Pipeline;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final Pipeline pipeline;

    @PostMapping("/login")
    public ResponseEntity<ResultRes<LoginResponse>> login(
            @RequestBody LoginParams params,
            HttpServletRequest request) {

        LoginCommand command = new LoginCommand();
        command.setUsernameOrEmail(params.getUsernameOrEmail());
        command.setPassword(params.getPassword());
        command.setIpAddress(request.getRemoteAddr());
        command.setDeviceName(request.getHeader("User-Agent"));

        TokenResponseDto tokenDto = command.execute(pipeline);

        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .accessTokenExpiry(tokenDto.getAccessTokenExpiry())
                .refreshTokenExpiry(tokenDto.getRefreshTokenExpiry())
                .build();

        return ResponseEntity.ok(
                ResultRes.successResult(loginResponse, AuthMessage.Login.SUCCESS, 200)
        );
    }
}
