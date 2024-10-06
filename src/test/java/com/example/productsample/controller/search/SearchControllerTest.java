package com.example.productsample.controller.search;

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

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class SearchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("(통합테스트) 카테고리별 최저가 상품 조회")
    void lowestPrice() throws Exception {
        mockMvc.perform(get("/api/lowest-price")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(200))
                .andExpect(jsonPath("$.resultMsg").value("OK"))
                .andExpect(jsonPath("$.result.최저가").exists())
                .andExpect(jsonPath("$.result.최저가.카테고리").isArray())
                .andExpect(jsonPath("$.result.최저가.카테고리[0].카테고리").value("상의"))
                .andExpect(jsonPath("$.result.최저가.카테고리[0].브랜드").value("C"))
                .andExpect(jsonPath("$.result.최저가.카테고리[0].가격").value("10,000"))
                .andExpect(jsonPath("$.result.최저가.카테고리[1].카테고리").value("아우터"))
                .andExpect(jsonPath("$.result.최저가.카테고리[1].브랜드").value("E"))
                .andExpect(jsonPath("$.result.최저가.카테고리[1].가격").value("5,000"))
                .andExpect(jsonPath("$.result.최저가.카테고리[2].카테고리").value("바지"))
                .andExpect(jsonPath("$.result.최저가.카테고리[2].브랜드").value("D"))
                .andExpect(jsonPath("$.result.최저가.카테고리[2].가격").value("3,000"))
                .andExpect(jsonPath("$.result.최저가.카테고리[3].카테고리").value("스니커즈"))
                .andExpect(jsonPath("$.result.최저가.카테고리[3].브랜드").value("G"))
                .andExpect(jsonPath("$.result.최저가.카테고리[3].가격").value("9,000"))
                .andExpect(jsonPath("$.result.최저가.카테고리[4].카테고리").value("가방"))
                .andExpect(jsonPath("$.result.최저가.카테고리[4].브랜드").value("A"))
                .andExpect(jsonPath("$.result.최저가.카테고리[4].가격").value("2,000"))
                .andExpect(jsonPath("$.result.최저가.카테고리[5].카테고리").value("모자"))
                .andExpect(jsonPath("$.result.최저가.카테고리[5].브랜드").value("D"))
                .andExpect(jsonPath("$.result.최저가.카테고리[5].가격").value("1,500"))
                .andExpect(jsonPath("$.result.최저가.카테고리[6].카테고리").value("양말"))
                .andExpect(jsonPath("$.result.최저가.카테고리[6].브랜드").value("I"))
                .andExpect(jsonPath("$.result.최저가.카테고리[6].가격").value("1,700"))
                .andExpect(jsonPath("$.result.최저가.카테고리[7].카테고리").value("액세서리"))
                .andExpect(jsonPath("$.result.최저가.카테고리[7].브랜드").value("F"))
                .andExpect(jsonPath("$.result.최저가.카테고리[7].가격").value("1,900"))
                .andExpect(jsonPath("$.result.최저가.총액").value("34,100"));
    }

    @Test
    @DisplayName("(통합테스트) 단일 브랜드 내 카테고리별 최저가 상품 조회")
    void lowestPriceOfBrands() throws Exception {
        mockMvc.perform(get("/api/lowest-price/brand")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(200))
                .andExpect(jsonPath("$.resultMsg").value("OK"))
                .andExpect(jsonPath("$.result.최저가").exists())
                .andExpect(jsonPath("$.result.최저가.브랜드").value("D"))
                .andExpect(jsonPath("$.result.최저가.카테고리[0].카테고리").value("상의"))
                .andExpect(jsonPath("$.result.최저가.카테고리[0].가격").value("10,100"))
                .andExpect(jsonPath("$.result.최저가.카테고리[1].카테고리").value("아우터"))
                .andExpect(jsonPath("$.result.최저가.카테고리[1].가격").value("5,100"))
                .andExpect(jsonPath("$.result.최저가.카테고리[2].카테고리").value("바지"))
                .andExpect(jsonPath("$.result.최저가.카테고리[2].가격").value("3,000"))
                .andExpect(jsonPath("$.result.최저가.카테고리[3].카테고리").value("스니커즈"))
                .andExpect(jsonPath("$.result.최저가.카테고리[3].가격").value("9,500"))
                .andExpect(jsonPath("$.result.최저가.카테고리[4].카테고리").value("가방"))
                .andExpect(jsonPath("$.result.최저가.카테고리[4].가격").value("2,500"))
                .andExpect(jsonPath("$.result.최저가.카테고리[5].카테고리").value("모자"))
                .andExpect(jsonPath("$.result.최저가.카테고리[5].가격").value("1,500"))
                .andExpect(jsonPath("$.result.최저가.카테고리[6].카테고리").value("양말"))
                .andExpect(jsonPath("$.result.최저가.카테고리[6].가격").value("2,400"))
                .andExpect(jsonPath("$.result.최저가.카테고리[7].카테고리").value("액세서리"))
                .andExpect(jsonPath("$.result.최저가.카테고리[7].가격").value("2,000"))
                .andExpect(jsonPath("$.result.최저가.총액").value("36,100"));
    }

    @Test
    @DisplayName("(통합테스트) 특정 카테고리 최저/최고가 상품 조회")
    void priceRangeByCategory() throws Exception {
        String categoryName = "상의";
        mockMvc.perform(get("/api/categories/{categoryName}/price-range", categoryName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(200))
                .andExpect(jsonPath("$.resultMsg").value("OK"))
                .andExpect(jsonPath("$.result.카테고리").value("상의"))
                .andExpect(jsonPath("$.result.최저가[0].브랜드").value("C"))
                .andExpect(jsonPath("$.result.최저가[0].가격").value("10,000"))
                .andExpect(jsonPath("$.result.최고가[0].브랜드").value("I"))
                .andExpect(jsonPath("$.result.최고가[0].가격").value("11,400"));
    }
}