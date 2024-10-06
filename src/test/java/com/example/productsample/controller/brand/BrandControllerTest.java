package com.example.productsample.controller.brand;

import com.example.productsample.controller.brand.request.CreateBrandRequest;
import com.example.productsample.controller.brand.request.UpdateBrandRequest;
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
class BrandControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("(통합테스트) 브랜드 추가")
    void createBrand() throws Exception {
        CreateBrandRequest createBrandRequest = new CreateBrandRequest();
        createBrandRequest.setBrandName("테스트 브랜드");

        mockMvc.perform(post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBrandRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(200))
                .andExpect(jsonPath("$.resultMsg").value("OK"))
                .andExpect(jsonPath("$.result.brandCd").value(10))
                .andExpect(jsonPath("$.result.brandName").value(createBrandRequest.getBrandName()));
    }

    @Test
    @DisplayName("(통합 테스트) 브랜드 업데이트")
    public void updateProduct() throws Exception {
        Long brandCd = 1L;

        UpdateBrandRequest updateBrandRequest = new UpdateBrandRequest();
        updateBrandRequest.setBrandName("테스트 브랜드");

        mockMvc.perform(patch("/api/brands/{brandCd}", brandCd)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBrandRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(200))
                .andExpect(jsonPath("$.resultMsg").value("OK"))
                .andExpect(jsonPath("$.result.brandCd").value(brandCd))
                .andExpect(jsonPath("$.result.brandName").value(updateBrandRequest.getBrandName()));
    }

    @Test
    @DisplayName("(통합 테스트) 브랜드 삭제")
    public void deleteProduct() throws Exception {
        Long brandCd = 1L;

        mockMvc.perform(delete("/api/brands/{brandCd}", brandCd)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(200))
                .andExpect(jsonPath("$.resultMsg").value("OK"));
    }
}