package com.example.productsample.service;

import com.example.productsample.controller.search.response.LowestPriceResponse;
import com.example.productsample.controller.search.response.LowestPriceResult;
import com.example.productsample.controller.search.response.PriceRangeResponse;
import com.example.productsample.controller.search.response.ProductInfo;
import com.example.productsample.enums.ErrorCode;
import com.example.productsample.exception.DefinedException;
import com.example.productsample.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private SearchService searchService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("(단위 테스트) 카테고리별 최저가 조회")
    public void lowestPrice() {
        // given
        List<Object[]> mockData = List.of(
                new Object[]{"카테고리1", "브랜드2", 1L, "Product2", 10500},
                new Object[]{"카테고리2", "브랜드1", 3L, "Product3", 5500}
        );

        when(productRepository.lowestPrice()).thenReturn(Optional.of(mockData));

        // when
        LowestPriceResponse response = searchService.lowestPrice();

        // then
        assertNotNull(response);
        LowestPriceResult result = response.getResult();
        assertNotNull(result);

        assertEquals("16,000", result.getTotalPrice());

        List<ProductInfo> productInfos = result.getProductInfos();
        assertEquals(2, productInfos.size());

        assertEquals("카테고리1", productInfos.getFirst().getCategoryName());
        assertEquals("브랜드2", productInfos.getFirst().getBrandName());
        assertEquals("10,500", productInfos.getFirst().getLowestPrice());
        assertEquals("카테고리2", productInfos.get(1).getCategoryName());
        assertEquals("브랜드1", productInfos.get(1).getBrandName());
        assertEquals("5,500", productInfos.get(1).getLowestPrice());

        verify(productRepository).lowestPrice();
    }

    @Test
    @DisplayName("(단위 테스트) 카테고리별 최저가 조회 (데이터 조회 실패)")
    public void lowestPrice_NotFound() {
        // given
        when(productRepository.lowestPrice()).thenReturn(Optional.empty());

        // when
        DefinedException exception = assertThrows(DefinedException.class, () ->
                searchService.lowestPrice());

        // then
        assertEquals(ErrorCode.NOT_FOUND_ENTITY, exception.getErrorCode(), exception.getMessage());
        verify(productRepository).lowestPrice();
    }

    @Test
    @DisplayName("(단위 테스트) 모든 카테고리 최저가격에 판매하는 브랜드")
    public void lowestPriceOfBrands() {
        // given
        List<Object[]> mockData = List.of(
                new Object[]{"카테고리1", "브랜드1", 1L, "Product2", 11200},
                new Object[]{"카테고리2", "브랜드1", 3L, "Product3", 7200}
        );

        when(productRepository.lowestPriceOfBrands()).thenReturn(Optional.of(mockData));

        // when
        LowestPriceResponse response = searchService.lowestPriceOfBrands();

        // then
        assertNotNull(response);
        LowestPriceResult result = response.getResult();
        assertNotNull(result);

        assertEquals("브랜드1", result.getBrandName());
        assertEquals("18,400", result.getTotalPrice());

        List<ProductInfo> productInfos = result.getProductInfos();
        assertEquals(2, productInfos.size());

        assertEquals("카테고리1", productInfos.get(0).getCategoryName());
        assertEquals("11,200", productInfos.get(0).getLowestPrice());
        assertEquals("카테고리2", productInfos.get(1).getCategoryName());
        assertEquals("7,200", productInfos.get(1).getLowestPrice());

        verify(productRepository).lowestPriceOfBrands();
    }

    @Test
    @DisplayName("(단위 테스트) 모든 카테고리 최저가격에 판매하는 브랜드 (데이터 조회 실패)")
    public void lowestPriceOfBrands_NotFound() {
        // given
        when(productRepository.lowestPrice()).thenReturn(Optional.empty());

        // when
        DefinedException exception = assertThrows(DefinedException.class, () ->
                searchService.lowestPriceOfBrands());

        // then
        assertEquals(ErrorCode.NOT_FOUND_ENTITY, exception.getErrorCode(), exception.getMessage());
        verify(productRepository).lowestPriceOfBrands();
    }

    @Test
    @DisplayName("(단위 테스트) 특정 카테고리 최저/최고가 상품 조회")
    public void priceRangeByCategory() {
        // given
        String categoryName = "카테고리1";
        Integer categoryCd = 1;
        when(categoryService.findCategoryCdByName(categoryName)).thenReturn(categoryCd);

        List<Object[]> mockData = List.of(
                new Object[]{"카테고리1", "브랜드1", 1L, "Product1", 10000, "MIN"},
                new Object[]{"카테고리1", "브랜드2", 2L, "Product2", 11400, "MAX"}
        );
        when(productRepository.priceRangeByCategory(categoryCd)).thenReturn(Optional.of(mockData));

        // when
        PriceRangeResponse response = searchService.priceRangeByCategory(categoryName);

        // then
        assertNotNull(response);
        assertEquals("카테고리1", response.getCategoryName());

        List<ProductInfo> minProductInfos = response.getMinProductInfo();
        assertEquals(1, response.getMinProductInfo().size());
        assertEquals("브랜드1", minProductInfos.getFirst().getBrandName());
        assertEquals("10,000", minProductInfos.getFirst().getLowestPrice());

        List<ProductInfo> maxProductInfos = response.getMaxProductInfo();
        assertEquals(1, response.getMinProductInfo().size());
        assertEquals("브랜드2", maxProductInfos.getFirst().getBrandName());
        assertEquals("11,400", maxProductInfos.getFirst().getLowestPrice());

        verify(productRepository, times(1)).priceRangeByCategory(categoryCd);
    }

    @Test
    @DisplayName("(단위 테스트) 특정 카테고리 최저/최고가 상품 조회 (데이터 조회 실패)")
    public void priceRangeByCategory_NotFound() {
        // given
        String categoryName = "카테고리1";
        Integer categoryCd = 1;
        when(categoryService.findCategoryCdByName(categoryName)).thenReturn(categoryCd);
        when(productRepository.priceRangeByCategory(categoryCd)).thenReturn(Optional.empty());

        // when
        DefinedException exception = assertThrows(DefinedException.class, () ->
                searchService.priceRangeByCategory(categoryName));

        // then
        assertEquals(ErrorCode.NOT_FOUND_ENTITY, exception.getErrorCode(), exception.getMessage());
        verify(productRepository).priceRangeByCategory(categoryCd);
    }

    @Test
    @DisplayName("(단위 테스트) 특정 카테고리 최저/최고가 상품 조회 (데이터 에러)")
    public void priceRangeByCategory_DataError() {
        String categoryName = "카테고리1";
        Integer categoryCd = 1;
        when(categoryService.findCategoryCdByName(categoryName)).thenReturn(categoryCd);

        List<Object[]> mockData = new ArrayList<>();
        mockData.add(new Object[]{"카테고리1", "브랜드1", 1L, "Product1", 10000, "MIN"});
        when(productRepository.priceRangeByCategory(categoryCd)).thenReturn(Optional.of(mockData));

        // when
        DefinedException exception = assertThrows(DefinedException.class, () ->
                searchService.priceRangeByCategory(categoryName));

        // then
        assertEquals(ErrorCode.DATA_ERROR, exception.getErrorCode(), exception.getMessage());
        verify(productRepository).priceRangeByCategory(categoryCd);
    }
}