package ai.omnicure.core.shared.constant.message.business;

public final class AuthMessage {

    private AuthMessage() {
        throw new UnsupportedOperationException("Constant class");
    }

    public static final class Login {
        private Login() {}
        public static final String SUCCESS = "Đăng nhập thành công";
        public static final String FAILED = "Tên đăng nhập hoặc mật khẩu không đúng";
        public static final String BANNED = "Tài khoản đã bị khóa, vui lòng liên hệ quản trị viên";
        public static final String UNVERIFIED = "Tài khoản chưa được xác minh";
        public static final String ERROR = "Đã xảy ra lỗi trong quá trình đăng nhập";
    }
}
