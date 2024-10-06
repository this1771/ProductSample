package com.example.productsample.service;

import com.example.productsample.component.CacheManage;
import com.example.productsample.controller.product.request.CreateProductRequest;
import com.example.productsample.controller.product.request.UpdateProductRequest;
import com.example.productsample.controller.product.response.ProductResponse;
import com.example.productsample.entity.BrandEntity;
import com.example.productsample.entity.CategoryEntity;
import com.example.productsample.entity.ProductEntity;
import com.example.productsample.enums.ErrorCode;
import com.example.productsample.exception.DefinedException;
import com.example.productsample.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CacheManage cacheManage;

    @Mock
    private BrandService brandService;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    private BrandEntity brandEntity;
    private CategoryEntity categoryEntity;
    private ProductEntity productEntity;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        brandEntity = new BrandEntity()
                .setBrandCd(1)
                .setBrandName("테스트 브랜드")
                .setDeleted(false);

        categoryEntity = new CategoryEntity()
                .setCategoryCd(1)
                .setCategoryName("테스트 카테고리");

        productEntity = new ProductEntity()
                .setProductCd(1L)
                .setBrandEntity(brandEntity)
                .setCategoryEntity(categoryEntity)
                .setProductName("테스트 상품")
                .setPrice(10000)
                .setDeleted(false);
    }

    @Test
    @DisplayName("(단위 테스트) 상품 추가")
    public void createProduct() {
        // given
        CreateProductRequest createProductRequest = new CreateProductRequest();
        createProductRequest.setBrandCd(1);
        createProductRequest.setCategoryCd(1);
        createProductRequest.setProductName("테스트 상품");
        createProductRequest.setPrice(10000);

        when(brandService.findByBrandCd(1)).thenReturn(brandEntity);
        when(categoryService.findByCategoryCd(1)).thenReturn(categoryEntity);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);

        // when
        ProductResponse response = productService.createProduct(createProductRequest);

        // then
        assertNotNull(response);
        assertEquals(1L, response.getProductCd());
        assertEquals(brandEntity.getBrandName(), response.getBrandName());
        assertEquals(categoryEntity.getCategoryName(), response.getCategoryName());
        assertEquals(productEntity.getProductName(), response.getProductName());
        assertEquals(productEntity.getPrice(), response.getPrice());
        verify(productRepository).save(any(ProductEntity.class));
    }

    @Test
    @DisplayName("(단위 테스트) 상품 업데이트")
    public void updateProduct() {
        Long productCd = 1L;
        UpdateProductRequest updateProductRequest = new UpdateProductRequest();
        updateProductRequest.setProductName("테스트 상품 업데이트");
        updateProductRequest.setPrice(20000);

        when(productRepository.findByProductCd(productCd)).thenReturn(Optional.of(productEntity));

        // when
        ProductResponse response = productService.updateProduct(productCd, updateProductRequest);

        assertNotNull(response);
        assertEquals(1L, response.getProductCd());
        assertEquals(brandEntity.getBrandName(), response.getBrandName());
        assertEquals(categoryEntity.getCategoryName(), response.getCategoryName());
        assertEquals(updateProductRequest.getProductName(), response.getProductName());
        assertEquals(updateProductRequest.getPrice(), response.getPrice());
        assertEquals(updateProductRequest.getProductName(), productEntity.getProductName());
        assertEquals(updateProductRequest.getPrice(), productEntity.getPrice());
    }

    @Test
    @DisplayName("(단위 테스트) 상품 삭제")
    public void deleteProduct() {
        // given
        Long productCd = 1L;
        when(productRepository.findByProductCd(productCd)).thenReturn(Optional.of(productEntity));
        doNothing().when(cacheManage).clearCache(anyString());

        // when
        productService.deleteProduct(productCd);

        // then
        assertTrue(productEntity.isDeleted());
        verify(productRepository).findByProductCd(productCd);
    }

    @Test
    @DisplayName("(단위 테스트) 상품 Entity 조회 실패 (데이터 존재하지 않는 경우)")
    public void findByProductCd_NotFound() {
        Long productCd = 1L;

        when(productRepository.findByProductCd(productCd)).thenReturn(Optional.empty());

        // when
        DefinedException exception = assertThrows(DefinedException.class, () ->
                productService.findByProductCd(productCd));

        // then
        assertEquals(ErrorCode.NOT_FOUND_PRODUCT, exception.getErrorCode(), exception.getMessage());
        verify(productRepository).findByProductCd(productCd);
    }

    @Test
    @DisplayName("(단위 테스트) 상품 Entity 조회 실패 (이미 삭제된 경우)")
    public void findByProductCd_AlreadyDeleted() {
        Long productCd = 1L;
        productEntity.setDeleted(true);

        when(productRepository.findByProductCd(productCd)).thenReturn(Optional.of(productEntity));

        // when
        DefinedException exception = assertThrows(DefinedException.class, () ->
                productService.findByProductCd(productCd));

        // then
        assertEquals(ErrorCode.ALREADY_DELETED_PRODUCT, exception.getErrorCode(), exception.getMessage());
        verify(productRepository).findByProductCd(productCd);
    }
}