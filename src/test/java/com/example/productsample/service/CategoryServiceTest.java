package com.example.productsample.service;

import com.example.productsample.controller.category.response.CategoryResponse;
import com.example.productsample.entity.CategoryEntity;
import com.example.productsample.enums.ErrorCode;
import com.example.productsample.exception.DefinedException;
import com.example.productsample.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("(단위 테스트) 카테고리 전체 목록")
    public void getAllCategories() {
        // given
        List<CategoryEntity> categoryEntities = List.of(
                new CategoryEntity().setCategoryCd(1).setCategoryName("카테고리1")
                , new CategoryEntity().setCategoryCd(2).setCategoryName("카테고리2")
        );

        when(categoryRepository.findAllByOrderByCategoryCdAsc()).thenReturn(categoryEntities);

        // when
        List<CategoryResponse> response = categoryService.getAllCategories();

        // then
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(1, response.get(0).getCategoryCd());
        assertEquals("카테고리1", response.get(0).getCategoryName());
        assertEquals(2, response.get(1).getCategoryCd());
        assertEquals("카테고리2", response.get(1).getCategoryName());
        verify(categoryRepository).findAllByOrderByCategoryCdAsc();
    }

    @Test
    @DisplayName("(단위 테스트) 카테고리 Entity 조회")
    public void findByCategoryCd() {
        // given
        Integer categoryCd = 1;
        CategoryEntity categoryEntity = new CategoryEntity().setCategoryCd(categoryCd).setCategoryName("카테고리1");

        when(categoryRepository.findByCategoryCd(categoryCd)).thenReturn(Optional.of(categoryEntity));

        // when
        CategoryEntity response = categoryService.findByCategoryCd(categoryCd);

        // then
        assertNotNull(response);
        assertEquals(categoryCd, response.getCategoryCd());
        assertEquals("카테고리1", response.getCategoryName());
        verify(categoryRepository).findByCategoryCd(categoryCd);
    }

    @Test
    @DisplayName("(단위 테스트) 카테고리 Entity 조회 (유효하지 않은 코드)")
    public void findByCategoryCd_Invalid() {
        // given
        Integer categoryCd = 1;

        when(categoryRepository.findByCategoryCd(categoryCd)).thenReturn(Optional.empty());

        // when
        DefinedException exception = assertThrows(DefinedException.class, () ->
                categoryService.findByCategoryCd(categoryCd));

        // then
        assertEquals(ErrorCode.INVALID_CATEGORY_CODE, exception.getErrorCode(), exception.getMessage());
        verify(categoryRepository).findByCategoryCd(categoryCd);
    }

    @Test
    @DisplayName("(단위 테스트) 카테고리 이름을 코드로 변환")
    public void findCategoryCdByName() {
        // given
        String categoryName = "카테고리1";
        CategoryEntity categoryEntity = new CategoryEntity().setCategoryCd(1).setCategoryName(categoryName);

        when(categoryRepository.findByCategoryName(categoryName)).thenReturn(Optional.of(categoryEntity));

        // when
        Integer response = categoryService.findCategoryCdByName(categoryName);

        // then
        assertNotNull(response);
        assertEquals(1, response);
        verify(categoryRepository).findByCategoryName(categoryName);
    }

    @Test
    @DisplayName("(단위 테스트) 카테고리 이름을 코드로 변환 (유효하지 않은 카테고리명)")
    public void findCategoryCdByName_Invalid() {
        // given
        String categoryName = "카테고리1";

        when(categoryRepository.findByCategoryName(categoryName)).thenReturn(Optional.empty());

        // when
        DefinedException exception = assertThrows(DefinedException.class, () ->
                categoryService.findCategoryCdByName(categoryName));

        // then
        assertEquals(ErrorCode.INVALID_CATEGORY_NAME, exception.getErrorCode(), exception.getMessage());
        verify(categoryRepository).findByCategoryName(categoryName);
    }
}