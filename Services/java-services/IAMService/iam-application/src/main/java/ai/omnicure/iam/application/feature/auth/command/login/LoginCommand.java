package ai.omnicure.iam.application.feature.auth.command.login;

import ai.omnicure.core.application.cqrs.Command;
import ai.omnicure.core.shared.constant.message.validation.AuthValidationMessage;
import ai.omnicure.iam.application.feature.auth.dto.TokenResponseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginCommand implements Command<TokenResponseDto> {

    @NotBlank(message = AuthValidationMessage.Login.USERNAME_OR_EMAIL_REQUIRED)
    private String usernameOrEmail;

    @NotBlank(message = AuthValidationMessage.Login.PASSWORD_REQUIRED)
    @Size(min = 6, message = AuthValidationMessage.Login.PASSWORD_MIN_LENGTH)
    @Size(max = 128, message = AuthValidationMessage.Login.PASSWORD_MAX_LENGTH)
    private String password;

    private String ipAddress;
    private String deviceName;
}
