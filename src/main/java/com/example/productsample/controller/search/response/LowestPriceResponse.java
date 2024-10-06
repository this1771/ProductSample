package com.example.productsample.controller.search.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class LowestPriceResponse  {
    @JsonProperty("최저가")
    private LowestPriceResult result;
}

