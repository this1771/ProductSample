package com.example.productsample.controller.search;

import com.example.productsample.controller.search.response.LowestPriceResponse;
import com.example.productsample.controller.search.response.PriceRangeResponse;
import com.example.productsample.model.ResponseModel;
import com.example.productsample.service.SearchService;
import com.example.productsample.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class SearchController {
    private final SearchService searchService;

    @GetMapping(value = "/lowest-price")
    public ResponseEntity<ResponseModel<LowestPriceResponse>> lowestPrice() {
        return ResponseUtils.successResult(searchService.lowestPrice());
    }

    @GetMapping(value = "/lowest-price/brand")
    public ResponseEntity<ResponseModel<LowestPriceResponse>> lowestPriceOfBrands() {
        return ResponseUtils.successResult(searchService.lowestPriceOfBrands());
    }

    @GetMapping(value = "/categories/{categoryName}/price-range")
    public ResponseEntity<ResponseModel<PriceRangeResponse>> priceRangeByCategory(
            @PathVariable String categoryName) {
        return ResponseUtils.successResult(searchService.priceRangeByCategory(categoryName));
    }
}
