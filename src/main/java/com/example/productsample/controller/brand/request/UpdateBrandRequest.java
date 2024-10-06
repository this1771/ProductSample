package com.example.productsample.controller.brand.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateBrandRequest {
    @Size(min = 1, max = 40, message = "Can be at least 1 character to 40 characters")
    private String brandName;
}
