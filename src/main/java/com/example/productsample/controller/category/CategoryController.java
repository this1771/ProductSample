package com.example.productsample.controller.category;

import com.example.productsample.controller.category.response.CategoryResponse;
import com.example.productsample.model.ResponseModel;
import com.example.productsample.service.CategoryService;
import com.example.productsample.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping(value = "/categories")
    public ResponseEntity<ResponseModel<List<CategoryResponse>>> getAllCategories() {
        return ResponseUtils.successResult(categoryService.getAllCategories());
    }
}
