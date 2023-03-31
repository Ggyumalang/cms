package com.zerobase.cms.order.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.model.ProductItem;
import com.zerobase.cms.order.service.ProductSearchService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SearchController.class)
class SearchControllerTest {

    @MockBean
    private ProductSearchService productSearchService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void searchByName() throws Exception {
        //given
        Product product = getProduct();
        given(productSearchService.searchByName(anyString()))
            .willReturn(List.of(product));
        //when
        //then
        mockMvc.perform(get("/search/product?name=khg"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("name"))
            .andExpect(jsonPath("$[0].description").value("test"));

    }

    @Test
    void getDetail() throws Exception {
        //given
        Product product = getProduct();
        given(productSearchService.getByProductId(anyLong()))
            .willReturn(product);
        //when
        //then
        mockMvc.perform(get("/search/product/detail?productId=1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("name"))
            .andExpect(jsonPath("$.description").value("test"))
            .andExpect(jsonPath("$.items[0].id").value(1))
            .andExpect(jsonPath("$.items[0].name").value("item"))
            .andExpect(jsonPath("$.items[0].price").value(1000))
            .andExpect(jsonPath("$.items[0].count").value(2));

    }

    private static Product getProduct() {
        return Product.builder()
            .id(1L)
            .sellerId(1L)
            .name("name")
            .description("test")
            .productItems(List.of(
                ProductItem.builder()
                    .id(1L)
                    .name("item")
                    .count(2L)
                    .price(1000L)
                    .sellerId(1L)
                    .build()
            ))
            .build();
    }
}