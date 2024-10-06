package com.example.productsample.controller.product;

import com.example.productsample.controller.product.request.CreateProductRequest;
import com.example.productsample.controller.product.request.UpdateProductRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("(통합테스트) 상품 추가")
    void createProduct() throws Exception {
        CreateProductRequest createProductRequest = new CreateProductRequest();
        createProductRequest.setBrandCd(1);
        createProductRequest.setCategoryCd(1);
        createProductRequest.setProductName("테스트 상품");
        createProductRequest.setPrice(10000);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createProductRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(200))
                .andExpect(jsonPath("$.resultMsg").value("OK"))
                .andExpect(jsonPath("$.result.productCd").value(73L))
                .andExpect(jsonPath("$.result.brandName").value("A"))
                .andExpect(jsonPath("$.result.categoryName").value("상의"))
                .andExpect(jsonPath("$.result.productName").value(createProductRequest.getProductName()))
                .andExpect(jsonPath("$.result.price").value(createProductRequest.getPrice()));
    }

    @Test
    @DisplayName("(통합 테스트) 상품 업데이트")
    public void updateProduct() throws Exception {
        Long productCd = 1L;

        UpdateProductRequest updateProductRequest = new UpdateProductRequest();
        updateProductRequest.setProductName("테스트 상품");
        updateProductRequest.setPrice(10000);

        mockMvc.perform(patch("/api/products/{productCd}", productCd)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProductRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(200))
                .andExpect(jsonPath("$.resultMsg").value("OK"))
                .andExpect(jsonPath("$.result.productCd").value(productCd))
                .andExpect(jsonPath("$.result.brandName").value("A"))
                .andExpect(jsonPath("$.result.categoryName").value("상의"))
                .andExpect(jsonPath("$.result.productName").value(updateProductRequest.getProductName()))
                .andExpect(jsonPath("$.result.price").value(updateProductRequest.getPrice()));
    }

    @Test
    @DisplayName("(통합 테스트) 상품 삭제")
    public void deleteProduct() throws Exception {
        Long productCd = 1L;

        mockMvc.perform(delete("/api/products/{productCd}", productCd)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(200))
                .andExpect(jsonPath("$.resultMsg").value("OK"));
    }
}