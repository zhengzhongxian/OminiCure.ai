package ai.omnicure.core.shared.constant.message.validation;

public final class AuthValidationMessage {

    private AuthValidationMessage() {
        throw new UnsupportedOperationException("Constant class");
    }

    public static final class Login {
        private Login() {}
        public static final String USERNAME_OR_EMAIL_REQUIRED = "Tên đăng nhập hoặc email không được để trống";
        public static final String PASSWORD_REQUIRED = "Mật khẩu không được để trống";
        public static final String PASSWORD_MIN_LENGTH = "Mật khẩu phải có tối thiểu 6 ký tự";
        public static final String PASSWORD_MAX_LENGTH = "Mật khẩu không được vượt quá 128 ký tự";
    }
}
