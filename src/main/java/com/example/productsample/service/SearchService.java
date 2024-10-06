package com.example.productsample.service;

import com.example.productsample.controller.search.response.LowestPriceResponse;
import com.example.productsample.controller.search.response.LowestPriceResult;
import com.example.productsample.controller.search.response.PriceRangeResponse;
import com.example.productsample.controller.search.response.ProductInfo;
import com.example.productsample.enums.ErrorCode;
import com.example.productsample.exception.DefinedException;
import com.example.productsample.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    // 프론트에서 comma 처리를 하지 않기 위해 사용.
    private final NumberFormat numberFormat = NumberFormat.getInstance(Locale.KOREA);

    @Cacheable(cacheNames = "lowestPrice")
    public LowestPriceResponse lowestPrice() {
        List<Object[]> result = productRepository.lowestPrice()
                .orElseThrow(() -> new DefinedException(ErrorCode.NOT_FOUND_ENTITY));

        int totalPrice = result.stream()
                .mapToInt(row -> (Integer) row[4])
                .sum();
        /*
          row[2] : productCd (option)
          row[3] : productName (option)
         */
        List<ProductInfo> productInfos = result.stream()
                .map(row -> {
                    return new ProductInfo()
                            .setCategoryName((String) row[0])               // categoryName
                            .setBrandName((String) row[1])                  // brandName
                            .setLowestPrice(numberFormat.format(row[4]));   // lowestPrice
                })
                .toList();

        LowestPriceResult lowestPriceResult = new LowestPriceResult()
                .setTotalPrice(numberFormat.format(totalPrice))
                .setProductInfos(productInfos);

        return new LowestPriceResponse().setResult(lowestPriceResult);
    }

    @Cacheable(cacheNames = "lowestPriceOfBrands")
    public LowestPriceResponse lowestPriceOfBrands() {
        List<Object[]> result = productRepository.lowestPriceOfBrands()
                .orElseThrow(() -> new DefinedException(ErrorCode.NOT_FOUND_ENTITY));

        // 최저가 및 브랜드 구하기
        Map.Entry<String, Integer> totalPriceByBrand = result.stream()
                .collect(Collectors.groupingBy(
                        row -> (String) row[1]
                        , Collectors.summingInt(row -> (Integer) row[4])
                ))
                .entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .orElseThrow(() -> new DefinedException(ErrorCode.NOT_FOUND_LOWEST_PRICE_BRAND));

        /*
          row[2] : productCd (option)
          row[3] : productName (option)
         */
        List<ProductInfo> productInfos = result.stream()
                .filter(row -> totalPriceByBrand.getKey().equals(row[1]))
                .map(row -> {
                    return new ProductInfo()
                            .setCategoryName((String) row[0])               // categoryName
                            .setLowestPrice(numberFormat.format(row[4]));   // lowestPrice
                })
                .toList();

        LowestPriceResult lowestPriceResult = new LowestPriceResult()
                .setBrandName(totalPriceByBrand.getKey())
                .setTotalPrice(numberFormat.format(totalPriceByBrand.getValue()))
                .setProductInfos(productInfos);

        return new LowestPriceResponse().setResult(lowestPriceResult);
    }

    @Cacheable(cacheNames = "priceRangeByCategory", condition = "#result != null")
    public PriceRangeResponse priceRangeByCategory(String categoryName) {
        Integer categoryCd = categoryService.findCategoryCdByName(categoryName);

        List<Object[]> result = productRepository.priceRangeByCategory(categoryCd)
                .orElseThrow(() -> new DefinedException(ErrorCode.NOT_FOUND_ENTITY));
        if (result.size() != 2) throw new DefinedException(ErrorCode.DATA_ERROR);

        List<ProductInfo> minProductInfos = findMinOrMaxProductInfos(result, "MIN");
        List<ProductInfo> maxProductInfos = findMinOrMaxProductInfos(result, "MAX");

        return new PriceRangeResponse()
                .setCategoryName(categoryName)
                .setMinProductInfo(minProductInfos)
                .setMaxProductInfo(maxProductInfos);
    }

    private List<ProductInfo> findMinOrMaxProductInfos(List<Object[]> productInfos, String type) {
        return productInfos.stream()
                .filter(row -> type.equals(row[5]))
                .map(row -> new ProductInfo()
                        .setBrandName((String) row[1])                 // brandName
                        .setLowestPrice(numberFormat.format(row[4]))   // price
                )
                .toList();
    }
}
