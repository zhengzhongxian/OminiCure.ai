package ai.omnicure.core.shared.constant.message.infra;

public final class DatabaseErrorMessage {
    private DatabaseErrorMessage() { 
        throw new UnsupportedOperationException("Constant class"); 
    }
    
    public static final String DUPLICATE = "Dữ liệu đã tồn tại hoặc bị trùng lặp";
    public static final String FOREIGN_KEY_VIOLATION = "Vi phạm khóa ngoại (Dữ liệu liên quan không tồn tại hoặc đang được sử dụng)";
    public static final String DATA_TOO_LONG = "Dữ liệu quá dài so với quy định cột";
    public static final String NOT_NULL_VIOLATION = "Không được phép để trống trường bắt buộc";
    public static final String DATA_TYPE_CONVERSION = "Định dạng dữ liệu không khớp";
    public static final String DEADLOCK = "Hệ thống đang quá tải, xảy ra bế tắc dữ liệu (Deadlock)";
    public static final String THROTTLED = "Lượt truy cập Database quá tải";
    public static final String CONNECTION_FAILED = "Mất kết nối tới Database";
    public static final String LOGIN_FAILED = "Xác thực Database thất bại";
    public static final String GENERAL = "Database System Error";
    public static final String UPDATE_ERROR = "Lỗi cập nhật CSDL";
    public static final String TIMEOUT = "Kết nối cơ sở dữ liệu quá hạn";
    public static final String CONCURRENCY_CONFLICT = "Xung đột dữ liệu đồng thời";
}
