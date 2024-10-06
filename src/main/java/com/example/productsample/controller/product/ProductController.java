package com.example.productsample.controller.product;

import com.example.productsample.controller.product.request.CreateProductRequest;
import com.example.productsample.controller.product.request.UpdateProductRequest;
import com.example.productsample.controller.product.response.ProductResponse;
import com.example.productsample.model.ResponseModel;
import com.example.productsample.service.ProductService;
import com.example.productsample.util.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class ProductController {
    private final ProductService productService;

    @PostMapping(value = "/products")
    public ResponseEntity<ResponseModel<ProductResponse>> createProduct(
            @Valid @RequestBody CreateProductRequest createProductRequest) {
        return ResponseUtils.successResult(productService.createProduct(createProductRequest));
    }

    @PatchMapping(value = "/products/{productCd}")
    public ResponseEntity<ResponseModel<ProductResponse>> updateProduct(
            @PathVariable("productCd") Long productCd
            , @Valid @RequestBody UpdateProductRequest updateProductRequest) {
        return ResponseUtils.successResult(productService.updateProduct(productCd, updateProductRequest));
    }

    @DeleteMapping(value = "/products/{productCd}")
    public ResponseEntity<ResponseModel<Void>> deleteProduct(
            @PathVariable("productCd") Long productCd) {
        productService.deleteProduct(productCd);
        return ResponseUtils.success();
    }
}
