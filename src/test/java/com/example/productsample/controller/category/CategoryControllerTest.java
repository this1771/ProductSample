package com.example.productsample.controller.category;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("(통합테스트) 카테고리 전체 목록")
    void lowestPriceOfBrands() throws Exception {
        mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(200))
                .andExpect(jsonPath("$.resultMsg").value("OK"))
                .andExpect(jsonPath("$.result[0].categoryCd").value(1))
                .andExpect(jsonPath("$.result[0].categoryName").value("상의"))
                .andExpect(jsonPath("$.result[1].categoryCd").value(2))
                .andExpect(jsonPath("$.result[1].categoryName").value("아우터"))
                .andExpect(jsonPath("$.result[2].categoryCd").value(3))
                .andExpect(jsonPath("$.result[2].categoryName").value("바지"))
                .andExpect(jsonPath("$.result[3].categoryCd").value(4))
                .andExpect(jsonPath("$.result[3].categoryName").value("스니커즈"))
                .andExpect(jsonPath("$.result[4].categoryCd").value(5))
                .andExpect(jsonPath("$.result[4].categoryName").value("가방"))
                .andExpect(jsonPath("$.result[5].categoryCd").value(6))
                .andExpect(jsonPath("$.result[5].categoryName").value("모자"))
                .andExpect(jsonPath("$.result[6].categoryCd").value(7))
                .andExpect(jsonPath("$.result[6].categoryName").value("양말"))
                .andExpect(jsonPath("$.result[7].categoryCd").value(8))
                .andExpect(jsonPath("$.result[7].categoryName").value("액세서리"));
    }
}