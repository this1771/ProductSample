package com.example.productsample.exception;

import com.example.productsample.enums.ErrorCode;
import lombok.Getter;

@Getter
public class DefinedException extends RuntimeException {
    private final ErrorCode errorCode;
    public DefinedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
