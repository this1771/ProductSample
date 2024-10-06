package com.example.productsample.util;

import com.example.productsample.enums.ErrorCode;
import com.example.productsample.exception.ErrorCodeResponse;
import com.example.productsample.model.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtils {
    public static ResponseEntity<ResponseModel<Void>> success() {
        return successMessage("OK");
    }

    public static ResponseEntity<ResponseModel<Void>> successMessage(String message) {
        return ResponseEntity.ok(
                new ResponseModel<>(
                        HttpStatus.OK.value(),
                        message
                ));
    }

    public static <T> ResponseEntity<ResponseModel<T>> successResult(T result) {
        return successMessageResult("OK", result);
    }

    public static <T> ResponseEntity<ResponseModel<T>> successMessageResult(String message, T result) {
        return ResponseEntity.ok(
                new ResponseModel<>(
                        HttpStatus.OK.value(),
                        message,
                        result
                ));
    }

    public static ResponseEntity<ErrorCodeResponse> failResult(HttpStatus httpStatus, ErrorCode errorCode) {
        return ResponseEntity.status(httpStatus.value())
                .body(new ErrorCodeResponse(
                        errorCode.getCode(),
                        errorCode.getMessage()
                ));
    }

    public static ResponseEntity<ErrorCodeResponse> failResult(HttpStatus httpStatus, int errorCode , String message) {
        return ResponseEntity.status(httpStatus.value())
                .body(new ErrorCodeResponse(
                        errorCode,
                        message
                ));
    }
}
