package com.example.productsample.service;

import com.example.productsample.component.CacheManage;
import com.example.productsample.controller.brand.request.CreateBrandRequest;
import com.example.productsample.controller.brand.request.UpdateBrandRequest;
import com.example.productsample.controller.brand.response.BrandResponse;
import com.example.productsample.entity.BrandEntity;
import com.example.productsample.enums.ErrorCode;
import com.example.productsample.exception.DefinedException;
import com.example.productsample.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    private final CacheManage cacheManage;

    @Transactional
    public BrandResponse createBrand(CreateBrandRequest createBrandRequest) {
        String brandName = createBrandRequest.getBrandName();

        if(existsByBrandName(brandName)) {
            throw new DefinedException(ErrorCode.UNAVAILABLE_BRAND);
        }

        BrandEntity brandEntity = new BrandEntity()
                .setBrandName(brandName);

        BrandEntity savedBrandEntity = brandRepository.save(brandEntity);

        return new BrandResponse()
                .setBrandCd(savedBrandEntity.getBrandCd())
                .setBrandName(savedBrandEntity.getBrandName());
    }

    @Transactional
    public BrandResponse updateBrand(Integer brandCd, UpdateBrandRequest updateBrandRequest) {
        BrandEntity brandEntity = findByBrandCd(brandCd);

        String updateBrandName = updateBrandRequest.getBrandName();
        if(updateBrandName != null) {
            if (!brandRepository.existsByBrandNameAndBrandCdNot(updateBrandName, brandCd)) {
                brandEntity.setBrandName(updateBrandName);
            } else {
                throw new DefinedException(ErrorCode.UNAVAILABLE_BRAND);
            }
        }

        return new BrandResponse()
                .setBrandCd(brandEntity.getBrandCd())
                .setBrandName(brandEntity.getBrandName());
    }

    @Transactional
    public void deleteBrand(Integer brandCd) {
        BrandEntity brandEntity = findByBrandCd(brandCd);
        brandEntity.setDeleted(true);

        // 브랜드 삭제시, cache 초기화
        cacheManage.clearCache("lowestPrice");
        cacheManage.clearCache("lowestPriceOfBrands");
        cacheManage.clearCache("priceRangeByCategory");
    }

    public BrandEntity findByBrandCd(Integer brandCd) {
        BrandEntity brandEntity = brandRepository.findByBrandCd(brandCd)
                .orElseThrow(() -> new DefinedException(ErrorCode.NOT_FOUND_BRAND));
        if(brandEntity.isDeleted()) throw new DefinedException(ErrorCode.ALREADY_DELETED_BRAND);

        return brandEntity;
    }

    public boolean existsByBrandName(String brandName) {
        return brandRepository.existsByBrandName(brandName);
    }
}
