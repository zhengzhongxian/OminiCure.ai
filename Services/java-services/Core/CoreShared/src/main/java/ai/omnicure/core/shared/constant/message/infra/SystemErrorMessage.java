package ai.omnicure.core.shared.constant.message.infra;

public final class SystemErrorMessage {
    private SystemErrorMessage() { 
        throw new UnsupportedOperationException("Constant class"); 
    }
    
    public static final class Request {
        private Request() {}
        public static final String CANCELLED = "Yêu cầu đã bị hủy bởi Client";
        public static final String INVALID_INPUT = "Dữ liệu đầu vào không hợp lệ";
        public static final String INVALID_OPERATION = "Invalid Operation";
    }

    public static final class External {
        private External() {}
        public static final String SERVICE_UNAVAILABLE = "Dịch vụ ngoài %s đang bảo trì hoặc lỗi mạng";
    }

    public static final String UNHANDLED_EXCEPTION = "Lỗi hệ thống ngoài dự kiến";
}
