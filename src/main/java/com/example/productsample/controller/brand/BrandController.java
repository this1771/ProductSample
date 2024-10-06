package com.example.productsample.controller.brand;

import com.example.productsample.controller.brand.request.CreateBrandRequest;
import com.example.productsample.controller.brand.request.UpdateBrandRequest;
import com.example.productsample.controller.brand.response.BrandResponse;
import com.example.productsample.model.ResponseModel;
import com.example.productsample.service.BrandService;
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
public class BrandController {
    private final BrandService brandService;

    @PostMapping(value = "/brands")
    public ResponseEntity<ResponseModel<BrandResponse>> createBrand(
            @Valid @RequestBody CreateBrandRequest createBrandRequest) {
        return ResponseUtils.successResult(brandService.createBrand(createBrandRequest));
    }

    @PatchMapping(value = "/brands/{brandCd}")
    public ResponseEntity<ResponseModel<BrandResponse>> updateBrand(
            @PathVariable("brandCd") Integer brandCd
            , @Valid @RequestBody UpdateBrandRequest updateBrandRequest) {
        return ResponseUtils.successResult(brandService.updateBrand(brandCd, updateBrandRequest));
    }

    @DeleteMapping(value = "/brands/{brandCd}")
    public ResponseEntity<ResponseModel<Void>> deleteBrand(
            @PathVariable("brandCd") Integer brandCd) {
        brandService.deleteBrand(brandCd);
        return ResponseUtils.success();
    }
}
