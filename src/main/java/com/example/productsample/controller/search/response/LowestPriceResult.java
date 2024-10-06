package com.example.productsample.controller.search.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LowestPriceResult {
    @JsonProperty("브랜드")
    private String brandName;
    @JsonProperty("카테고리")
    private List<ProductInfo> productInfos;
    @JsonProperty("총액")
    private String totalPrice;
}

