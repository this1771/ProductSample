package com.example.productsample.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseModel<T> {
    private final int resultCode;
    private final String resultMsg;
    private T result;

    public ResponseModel(int status, String message) {
        this.resultCode = status;
        this.resultMsg = message;
    }

    public ResponseModel(int status, String message, T result) {
        this.resultCode = status;
        this.resultMsg = message;
        this.result = result;
    }
}
