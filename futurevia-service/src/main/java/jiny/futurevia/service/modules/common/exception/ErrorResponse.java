package jiny.futurevia.service.modules.common.exception;


import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * {
 * "code" : "400",
 * "message" : "잘못된 요청입니다"
 * "validation" : {
 * "title" : "값을 입력해주세요"
 * }
 * }
 */
@Getter
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

    private final String code;
    private final String message;
    private final Map<String, String> validations = new HashMap<>();

    public void addValidation(String fieldName, String message) {
        validations.put(fieldName, message);
    }

    @Builder
    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

//    @RequiredArgsConstructor
//    private class ValidationTuple {
//        private final String fieldName;
//        private final String errorMessage;
//    }
}
