package com.example.productsample.service;

import com.example.productsample.controller.category.response.CategoryResponse;
import com.example.productsample.entity.CategoryEntity;
import com.example.productsample.enums.ErrorCode;
import com.example.productsample.exception.DefinedException;
import com.example.productsample.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Cacheable(cacheNames = "allCategories")
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAllByOrderByCategoryCdAsc().stream()
                .map(category ->
                        new CategoryResponse(
                                category.getCategoryCd()
                                , category.getCategoryName()
                        ))
                .toList();

    }

    public CategoryEntity findByCategoryCd(Integer categoryCd) {
        return categoryRepository.findByCategoryCd(categoryCd)
                .orElseThrow(() -> new DefinedException(ErrorCode.INVALID_CATEGORY_CODE));
    }

    @Cacheable(cacheNames = "categoryCodeByName", condition = "#result != null")
    public Integer findCategoryCdByName(String categoryName) {
        CategoryEntity category = categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new DefinedException(ErrorCode.INVALID_CATEGORY_NAME));
        return category.getCategoryCd();
    }
}
