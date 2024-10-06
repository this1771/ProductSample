package com.example.productsample.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {
    LOWEST_PRICE("lowestPrice",  60, 1),
    LOWEST_PRICE_BRANDS("lowestPriceOfBrands",  60, 1),

    ALL_CATEGORIES("allCategories",  60, 1),

    // TODO 카테고리 개수가 증가하면 maxSize 늘려줄 것.
    CATEGORY_CODE_BY_NAME("categoryCodeByName",  60, 8),
    CATEGORY_PRICE_RANGE("priceRangeByCategory", 60, 8);

    private final String cacheName;     // 캐시명
    private final int expireTime;       // 만료시간 (단위 : 초)
    private final int maxSize;          // 저장 가능한 최대 개수
}
