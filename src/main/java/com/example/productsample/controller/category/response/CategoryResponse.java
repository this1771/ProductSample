package com.example.productsample.controller.category.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@Accessors(chain = true)
public class CategoryResponse {
    private Integer categoryCd;
    private String categoryName;
}
