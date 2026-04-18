package ai.omnicure.iam.api.model.request.v1;

import lombok.Data;

@Data
public class LoginParams {
    private String usernameOrEmail;
    private String password;
}
