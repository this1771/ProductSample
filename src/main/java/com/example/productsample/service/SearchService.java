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

    /*
     * Note
     * 1. 브랜드 및 상품 추가 / 수정시 cache를 삭제하지 않은 이유는
     *    상품 갯수가 많이질 시 퍼포먼스가 떨어질 수 있다고 판단되어 삭제시에만 처리.
     *    문제가 된다면, 추가/수정시에도 캐시를 삭제하도록 처리 필요
     *
     * 2. DB에서 최저가 목록을 가져오는 이유는, 같은 브랜드/카테고리 내 동일한 가격을 가진 상품이 존재할 경우
     *    실제 어떤 상품(productCd)인지 제대로 파악할 수 없기 떄문이며, 코드로 처리할 경우 유지보수성이 떨어질 것으로 판단되어
     *    PARTITION BY를 이용하여 값을 계산하게 처리. (단, mysql 5.7까지는 해당 함수를 지원하지 않으니 주의)
     */
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

    /*
     * Note
     * 1. cache 처리는 lowestPrice와 동일
     *
     * 2. 브랜드 최저가를 구할시, DB에서 쿼리를 한번 더 싫행하여 최저가 브랜드 및 총합을 계산할 수 있었으나,
     *    데이터 조회하는 사이에 정보가 변경될 수도 있기 떄문에 정합성 면에서 좋지 않다고 판단하여 코드로 처리.
     *    브랜드 및 조회한 데이터가 많아질 경우를 대비해서 batchjob 고려 필요.
     */
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
