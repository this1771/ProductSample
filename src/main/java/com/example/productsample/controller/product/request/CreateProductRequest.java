package com.example.productsample.controller.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateProductRequest {

    @NotNull(message = "Required value.")
    private Integer brandCd;

    @NotNull(message = "Required value.")
    private Integer categoryCd;

    @NotBlank(message = "Required value.")
    @Size(min = 1, max = 100, message = "Can be at least 1 character to 100 characters")
    private String productName;

    @NotNull(message = "Required value.")
    @Positive(message = "Must be a positive value.")
    private Integer price;
}
