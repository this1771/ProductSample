package com.example.productsample.controller.product.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateProductRequest {
    @Size(min = 1, max = 100, message = "Can be at least 1 character to 100 characters")
    private String productName;

    @Positive(message = "Must be a positive value.")
    private Integer price;
}
