package com.example.productsample.controller.search.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class PriceRangeResponse {
    @JsonProperty("카테고리")
    private String categoryName;
    @JsonProperty("최저가")
    private List<ProductInfo> minProductInfo;
    @JsonProperty("최고가")
    private List<ProductInfo> maxProductInfo;
}
