package com.example.productsample.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 정의되지 않은 에러 (중요)
    UNDEFINED_ERROR(9999,  "An unexpected error occurred. Please try again later."),
    // 허용하지 않은 HTTP Method
    NOT_ALLOWED_METHOD(9001, "Unsupported API methods."),
    // 데이터 찾을 수 없음
    NOT_FOUND_ENTITY(9002, "Entity not found."),
    // 잘못된 요청
    BAD_REQUEST(9003, "Bad request."),
    // JPA 무결성 제약 문제
    CONSTRAINT_VIOLATION(9004, "Violates persistence constraints."),
    // 잘못된 입력 포맷
    INVALID_INPUT_FORMAT(9005,"Invalid input format."),
    // 요청 본문이 비어있음.
    REQUEST_BODY_EMPTY(9006,"Request body is empty."),
    // 데이터 문제 발생
    DATA_ERROR(9007, "There is a problem with the data"),

    // 유효하지 않은 카테고리명
    INVALID_CATEGORY_NAME(1001,"Invalid category name."),
    // 유효하지 않은 카테고리 코드
    INVALID_CATEGORY_CODE(1002,"Invalid category code."),

    // 사용할 수 없는 브랜드명
    UNAVAILABLE_BRAND(2001, "Unavailable brandName."),
    // 브랜드 정보를 찾을 수 없음
    NOT_FOUND_BRAND(2002, "Brand not found."),
    // 이미 삭제된 브랜드
    ALREADY_DELETED_BRAND(2003, "The brand has already been deleted."),

    // 유효(삭제 OR 미존재)하지 않는 상품 코드
    NOT_FOUND_PRODUCT(3001, "Invalid productCd"),
    // 이미 삭제된 상품
    ALREADY_DELETED_PRODUCT(3002, "The Product has already been deleted."),

    // 최저가 브랜드를 찾을 수 없음
    NOT_FOUND_LOWEST_PRICE_BRAND(4001,"No lowest priced brands found.");


    private final int code;
    private final String message;
}
