package jiny.futurevia.service.modules.common.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    Boolean result;
    T message;

    String errorCode;
    String errorMessage;
    Map<String, String> fieldErrors = new HashMap<>();

    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    public static <T> ApiResponse<T> success(T message) {
        return ApiResponse.<T>builder()
                .result(true)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> error(String errorCode, String errorMessage) {
        return ApiResponse.<T>builder()
                .result(false)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build();
    }

    public static <T> ResponseEntity<ApiResponse<T>> serverError(String errorCode, String errorMessage) {
        return ResponseEntity.status(500).body(ApiResponse.<T>builder()
                .result(false)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build());
    }

    public void addFieldError(String fieldName, String errors) {
        fieldErrors.put(fieldName, errors);
    }

}