package com.example.productsample.controller.search.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductInfo {
    @JsonProperty("카테고리")
    private String categoryName;
    @JsonProperty("브랜드")
    private String brandName;
    @JsonProperty("상품코드")
    private Long productCd;
    @JsonProperty("상품명")
    private String productName;
    @JsonProperty("가격")
    private String lowestPrice;
}
