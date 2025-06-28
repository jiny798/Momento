package jiny.futurevia.service.modules.common.exception;


import jiny.futurevia.service.modules.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ApiResponse<Void> handleException(MethodArgumentNotValidException e) {
        ApiResponse<Void> apiResponse = ApiResponse.error(String.valueOf(HttpStatus.BAD_REQUEST.value()), "잘못된 입력값 입니다.");

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            apiResponse.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return apiResponse;
    }

    @ExceptionHandler(CommonException.class)
    public ApiResponse<Void> handleException(CommonException e) {
        int statusCode = e.getStatusCode();

        return ApiResponse.error(
                String.valueOf(statusCode),
                e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> serverException(Exception ex) {
        // TODO : 서버 에러 알림 기능 추가
        return ApiResponse.serverError(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getMessage());
    }



}










