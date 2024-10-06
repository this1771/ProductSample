package com.example.productsample.service;

import com.example.productsample.component.CacheManage;
import com.example.productsample.controller.brand.request.CreateBrandRequest;
import com.example.productsample.controller.brand.request.UpdateBrandRequest;
import com.example.productsample.controller.brand.response.BrandResponse;
import com.example.productsample.entity.BrandEntity;
import com.example.productsample.enums.ErrorCode;
import com.example.productsample.exception.DefinedException;
import com.example.productsample.repository.BrandRepository;
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

class BrandServiceTest {
    @Mock
    private BrandRepository brandRepository;

    @Mock
    private CacheManage cacheManage;

    @InjectMocks
    private BrandService brandService;

    private BrandEntity brandEntity;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        brandEntity = new BrandEntity()
                .setBrandCd(1)
                .setBrandName("테스트 브랜드")
                .setDeleted(false);
    }

    @Test
    @DisplayName("(단위 테스트) 브랜드 추가")
    public void createBrand() {
        // given
        CreateBrandRequest createBrandRequest = new CreateBrandRequest();
        createBrandRequest.setBrandName("테스트 브랜드");

        when(brandRepository.existsByBrandName(anyString())).thenReturn(false);
        when(brandRepository.save(any(BrandEntity.class))).thenReturn(brandEntity);

        // when
        BrandResponse response = brandService.createBrand(createBrandRequest);

        // then
        assertNotNull(response);
        assertEquals(1, response.getBrandCd());
        assertEquals(createBrandRequest.getBrandName(), response.getBrandName());
        verify(brandRepository).save(any(BrandEntity.class));
    }

    @Test
    @DisplayName("(단위 테스트) 브랜드 추가 실패 (브랜드명 존재하는 경우)")
    public void createBrand_AlreadyExist() {
        // given
        CreateBrandRequest createBrandRequest = new CreateBrandRequest();
        createBrandRequest.setBrandName("테스트 브랜드");

        when(brandRepository.existsByBrandName(anyString())).thenReturn(true).thenThrow();

        // when
        DefinedException exception = assertThrows(DefinedException.class, () ->
                brandService.createBrand(createBrandRequest));

        // then
        assertEquals(ErrorCode.UNAVAILABLE_BRAND, exception.getErrorCode(), exception.getMessage());
        verify(brandRepository, never()).save(any(BrandEntity.class));
    }

    @Test
    @DisplayName("(단위 테스트) 브랜드 업데이트")
    public void updateBrand() {
        // given
        Integer brandCd = 1;
        String updateBrandName = "테스트 브랜드2";
        UpdateBrandRequest updateBrandRequest = new UpdateBrandRequest();
        updateBrandRequest.setBrandName(updateBrandName);

        when(brandRepository.findByBrandCd(brandCd)).thenReturn(Optional.of(brandEntity));
        when(brandRepository.existsByBrandNameAndBrandCdNot(updateBrandName, brandCd)).thenReturn(false);

        // when
        BrandResponse response = brandService.updateBrand(brandCd, updateBrandRequest);

        // then
        assertNotNull(response);
        assertEquals(1, response.getBrandCd());
        assertEquals(updateBrandRequest.getBrandName(), response.getBrandName());
        assertEquals(updateBrandRequest.getBrandName(), brandEntity.getBrandName());
    }

    @Test
    @DisplayName("(단위 테스트) 브랜드 업데이트 실패 (브랜드명 존재하는 경우)")
    public void updateBrand_AlreadyExist() {
        // given
        Integer brandCd = 1;
        String updateBrandName = "테스트 브랜드";
        UpdateBrandRequest updateBrandRequest = new UpdateBrandRequest();
        updateBrandRequest.setBrandName(updateBrandName);

        when(brandRepository.findByBrandCd(brandCd)).thenReturn(Optional.of(brandEntity));
        when(brandRepository.existsByBrandNameAndBrandCdNot(updateBrandName, brandCd)).thenReturn(true);

        // when
        DefinedException exception = assertThrows(DefinedException.class, () ->
                brandService.updateBrand(brandCd, updateBrandRequest));

        // then
        assertEquals(ErrorCode.UNAVAILABLE_BRAND, exception.getErrorCode(), exception.getMessage());
        assertEquals(updateBrandRequest.getBrandName(), brandEntity.getBrandName());
    }

    @Test
    @DisplayName("(단위 테스트) 브랜드 삭제")
    public void deleteBrand() {
        // given
        Integer brandCd = 1;

        when(brandRepository.findByBrandCd(brandCd)).thenReturn(Optional.of(brandEntity));
        doNothing().when(cacheManage).clearCache(anyString());

        // when
        brandService.deleteBrand(brandCd);

        // then
        assertTrue(brandEntity.isDeleted());
        verify(brandRepository).findByBrandCd(brandCd);
    }

    @Test
    @DisplayName("(단위 테스트) 브랜드 Entity 조회 (데이터 존재하지 않는 경우)")
    public void findByBrandCd_NotFound() {
        // given
        Integer brandCd = 1;

        when(brandRepository.findByBrandCd(brandCd)).thenReturn(Optional.empty());

        // when
        DefinedException exception = assertThrows(DefinedException.class, () ->
                brandService.findByBrandCd(brandCd));

        // then
        assertEquals(ErrorCode.NOT_FOUND_BRAND, exception.getErrorCode(), exception.getMessage());
        verify(brandRepository).findByBrandCd(brandCd);
    }

    @Test
    @DisplayName("(단위 테스트) 브랜드 Entity 조회 (이미 삭제된 경우)")
    public void findByBrandCd_AlreadyDeleted() {
        // given
        Integer brandCd = 1;
        brandEntity.setDeleted(true);

        when(brandRepository.findByBrandCd(brandCd)).thenReturn(Optional.of(brandEntity));

        // when
        DefinedException exception = assertThrows(DefinedException.class, () ->
                brandService.findByBrandCd(brandCd));

        // then
        assertEquals(ErrorCode.ALREADY_DELETED_BRAND, exception.getErrorCode(), exception.getMessage());
        verify(brandRepository).findByBrandCd(brandCd);
    }
}