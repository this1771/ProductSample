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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final CacheManage cacheManage;


    @Transactional
    public ProductResponse createProduct(CreateProductRequest createProductRequest) {
        BrandEntity brandEntity = brandService.findByBrandCd(createProductRequest.getBrandCd());
        CategoryEntity categoryEntity = categoryService.findByCategoryCd(createProductRequest.getCategoryCd());

        ProductEntity productEntity = new ProductEntity()
                .setBrandEntity(brandEntity)
                .setCategoryEntity(categoryEntity)
                .setProductName(createProductRequest.getProductName())
                .setPrice(createProductRequest.getPrice());
        ProductEntity savedProductEntity = productRepository.save(productEntity);

        return new ProductResponse()
                .setProductCd(savedProductEntity.getProductCd())
                .setBrandName(savedProductEntity.getBrandEntity().getBrandName())
                .setCategoryName(savedProductEntity.getCategoryEntity().getCategoryName())
                .setProductName(savedProductEntity.getProductName())
                .setPrice(savedProductEntity.getPrice());
    }

    @Transactional
    public ProductResponse updateProduct(Long productCd, UpdateProductRequest updateProductRequest) {
        ProductEntity productEntity = findByProductCd(productCd);

        String updateProductName = updateProductRequest.getProductName();
        Integer price = updateProductRequest.getPrice();

        if(updateProductName != null) {
            productEntity.setProductName(updateProductName);
        }

        if(price != null) {
            productEntity.setPrice(price);
        }

        return new ProductResponse()
                .setProductCd(productEntity.getProductCd())
                .setBrandName(productEntity.getBrandEntity().getBrandName())
                .setCategoryName(productEntity.getCategoryEntity().getCategoryName())
                .setProductName(productEntity.getProductName())
                .setPrice(productEntity.getPrice());
    }

    @Transactional
    public void deleteProduct(Long productCd) {
        ProductEntity productEntity = findByProductCd(productCd);
        productEntity.setDeleted(true);

        // 상품 삭제시, cache 초기화
        cacheManage.clearCache("lowestPrice");
        cacheManage.clearCache("lowestPriceOfBrands");
        cacheManage.clearCache("priceRangeByCategory");
    }

    public ProductEntity findByProductCd(Long productCd) {
        ProductEntity productEntity = productRepository.findByProductCd(productCd)
                .orElseThrow(() -> new DefinedException(ErrorCode.NOT_FOUND_PRODUCT));
        if(productEntity.isDeleted()) throw new DefinedException(ErrorCode.ALREADY_DELETED_PRODUCT);

        return productEntity;
    }
}
