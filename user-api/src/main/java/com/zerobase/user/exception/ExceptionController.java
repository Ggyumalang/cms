package com.zerobase.user.exception;

import static com.zerobase.user.exception.ErrorCode.HTTP_MESSAGE_NOT_READABLE;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException re) {
        log.error("HttpMessageNotReadableException is occurred", re);
        return ResponseEntity.badRequest().body(new ExceptionResponse(
            re.getMessage(), HTTP_MESSAGE_NOT_READABLE));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException me) {
        Map<String, String> errors = new HashMap<>();
        me.getBindingResult().getAllErrors()
            .forEach(c -> errors.put(((FieldError) c).getField(),
                c.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponse> handleCustomException(
        CustomException ce) {
        log.warn("api Exception : {}", ce.getErrorCode());
        return ResponseEntity.badRequest()
            .body(new ExceptionResponse(ce.getMessage(), ce.getErrorCode()));
    }

    @Getter
    @ToString
    @AllArgsConstructor
    public static class ExceptionResponse {

        private String message;
        private ErrorCode errorCode;
    }
}
