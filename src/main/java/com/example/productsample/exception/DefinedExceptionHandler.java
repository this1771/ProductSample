package com.example.productsample.exception;

import com.example.productsample.enums.ErrorCode;
import com.example.productsample.util.ResponseUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class DefinedExceptionHandler {
    @ExceptionHandler(DefinedException.class)
    public ResponseEntity<ErrorCodeResponse> customException(
            HttpServletRequest request
            , DefinedException e) {
        logException(request, e);
        return ResponseUtils.failResult(HttpStatus.BAD_REQUEST, e.getErrorCode());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorCodeResponse> httpRequestMethodNotSupportedException(
            HttpServletRequest request
            , HttpRequestMethodNotSupportedException e) {
        logException(request, e);
        return ResponseUtils.failResult(HttpStatus.METHOD_NOT_ALLOWED, ErrorCode.NOT_ALLOWED_METHOD);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorCodeResponse> httpMessageNotReadableException(
            HttpServletRequest request
            , HttpMessageNotReadableException e) {
        logException(request, e);
        return ResponseUtils.failResult(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_INPUT_FORMAT);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorCodeResponse> httpMessageNotReadableException(
            HttpServletRequest request
            , MethodArgumentTypeMismatchException e) {
        logException(request, e);
        return ResponseUtils.failResult(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_INPUT_FORMAT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorCodeResponse> methodArgumentNotValidException(
            HttpServletRequest request
            , MethodArgumentNotValidException e) {
        logException(request, e);
        String errorMessage = getErrorMessage(e.getBindingResult());
        return ResponseUtils.failResult(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), errorMessage);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorCodeResponse> entityNotFoundException(
            HttpServletRequest request
            , EntityNotFoundException e) {
        logException(request, e);
        return ResponseUtils.failResult(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_ENTITY);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorCodeResponse> badRequestException(
            HttpServletRequest request
            , BadRequestException e) {
        logException(request, e);
        return ResponseUtils.failResult(HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorCodeResponse> constraintViolationException(
            HttpServletRequest request
            , ConstraintViolationException e) {
        logException(request, e);
        return ResponseUtils.failResult(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.CONSTRAINT_VIOLATION);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> handleNoResourceFoundException(
            HttpServletRequest request
            , NoResourceFoundException e) {
        logException(request, e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorCodeResponse> exception(
            HttpServletRequest request
            , Exception e) {
        logException(request, e);
        return ResponseUtils.failResult(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.UNDEFINED_ERROR);
    }

    private String getErrorMessage(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .findFirst()
                .map(fieldError -> String.format("'%s': %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .orElse("Validation error occurred");
    }

    private void logException(HttpServletRequest request, Exception e) {
        log.error("Exception: [{}], URI: [{}], method: [{}], error message: [{}]"
                , e.getClass().getSimpleName()
                , request.getRequestURI()
                , request.getMethod()
                , e.getMessage() != null ? e.getMessage() : "");
    }
}
