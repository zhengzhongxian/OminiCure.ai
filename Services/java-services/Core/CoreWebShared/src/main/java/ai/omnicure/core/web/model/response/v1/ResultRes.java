package ai.omnicure.core.web.model.response.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultRes<T> {

    @Builder.Default
    private boolean success = true;

    @Builder.Default
    private String message = "Success";

    private T data;

    @Builder.Default
    private List<String> errors = Collections.emptyList();

    @Builder.Default
    private int statusCode = 200;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public static <T> ResultRes<T> successResult(T data) {
        return successResult(data, "Success", 200);
    }

    public static <T> ResultRes<T> successResult(T data, String message, int statusCode) {
        return ResultRes.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .statusCode(statusCode)
                .build();
    }

    public static <T> ResultRes<T> errorResult(T data, String message, int statusCode, List<String> errors) {
        return ResultRes.<T>builder()
                .success(false)
                .message(message)
                .data(data)
                .statusCode(statusCode)
                .errors(errors != null ? errors : Collections.emptyList())
                .build();
    }

    public static ResultRes<Object> errorResult(String message, int statusCode, List<String> errors) {
        return ResultRes.builder()
                .success(false)
                .message(message)
                .data(null)
                .statusCode(statusCode)
                .errors(errors != null ? errors : Collections.emptyList())
                .build();
    }

    public static ResultRes<Object> successResult(String message, int statusCode) {
        return ResultRes.builder()
                .success(true)
                .message(message)
                .data(null)
                .statusCode(statusCode)
                .build();
    }
}
