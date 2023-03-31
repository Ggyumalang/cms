package com.zerobase.cms.order.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.model.ProductItem;
import com.zerobase.cms.order.domain.product.AddProductForm;
import com.zerobase.cms.order.domain.product.AddProductItemForm;
import com.zerobase.cms.order.domain.product.UpdateProductForm;
import com.zerobase.cms.order.domain.product.UpdateProductItemForm;
import com.zerobase.cms.order.service.ProductItemService;
import com.zerobase.cms.order.service.ProductService;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import com.zerobase.domain.domain.common.UserVo;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SellerProductController.class)
class SellerProductControllerTest {

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductItemService productItemService;

    @MockBean
    private JwtAuthenticationProvider provider;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addProduct() throws Exception {
        //given
        given(provider.getUserVo(anyString()))
            .willReturn(new UserVo(1L, "khg5087@email.com"));

        Product product = getProduct();
        given(productService.addProduct(anyLong(), any()))
            .willReturn(product);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCY0ROWnZCMVhCZ3d2QVJBUjhOYUlzOG9Uc2JSR0NCd0VLOGlCRUdsZVFVPSIsImp0aSI6IkNtWUR2NU1LNVlVeWlrOEtHTkR3b3c9PSIsInJvbGVzIjoiQ1VTVE9NRVIiLCJpYXQiOjE2ODAyMjY3MDcsImV4cCI6MTY4MDMxMzEwN30.otlY25cTA9MLKRvKsXLGxpC5oj_-67V6W-XEywt6Les");
        headers.add("Content-Type", "application/json;charset=UTF-8");
        //when
        //then
        mockMvc.perform(post("/seller/product")
                .headers(headers)
                .content(objectMapper.writeValueAsString(
                    AddProductForm.builder()
                        .name("name")
                        .description("descripton")
                        .items(List.of(
                            AddProductItemForm.builder().build()))
                        .build()
                )))
            .andDo(print())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("name"))
            .andExpect(jsonPath("$.description").value("test"))
            .andExpect(jsonPath("$.items[0].id").value(1))
            .andExpect(jsonPath("$.items[0].name").value("item"))
            .andExpect(jsonPath("$.items[0].price").value(1000))
            .andExpect(jsonPath("$.items[0].count").value(2));
    }

    @Test
    void addProductItem() throws Exception {
        //given
        given(provider.getUserVo(anyString()))
            .willReturn(new UserVo(1L, "khg5087@email.com"));

        Product product = getProduct();
        given(productItemService.addProductItem(anyLong(), any()))
            .willReturn(product);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCY0ROWnZCMVhCZ3d2QVJBUjhOYUlzOG9Uc2JSR0NCd0VLOGlCRUdsZVFVPSIsImp0aSI6IkNtWUR2NU1LNVlVeWlrOEtHTkR3b3c9PSIsInJvbGVzIjoiQ1VTVE9NRVIiLCJpYXQiOjE2ODAyMjY3MDcsImV4cCI6MTY4MDMxMzEwN30.otlY25cTA9MLKRvKsXLGxpC5oj_-67V6W-XEywt6Les");
        headers.add("Content-Type", "application/json;charset=UTF-8");

        //when
        //then
        mockMvc.perform(post("/seller/product/item")
                .headers(headers)
                .content(objectMapper.writeValueAsString(
                    AddProductItemForm.builder()
                        .productId(1L)
                        .name("name")
                        .price(1000L)
                        .count(1L)
                        .build()
                )))
            .andDo(print())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("name"))
            .andExpect(jsonPath("$.description").value("test"))
            .andExpect(jsonPath("$.items[0].id").value(1))
            .andExpect(jsonPath("$.items[0].name").value("item"))
            .andExpect(jsonPath("$.items[0].price").value(1000))
            .andExpect(jsonPath("$.items[0].count").value(2));
    }

    @Test
    void updateProduct() throws Exception {
        //given
        given(provider.getUserVo(anyString()))
            .willReturn(new UserVo(1L, "khg5087@email.com"));

        Product product = getProduct();
        given(productService.updateProduct(anyLong(), any()))
            .willReturn(product);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCY0ROWnZCMVhCZ3d2QVJBUjhOYUlzOG9Uc2JSR0NCd0VLOGlCRUdsZVFVPSIsImp0aSI6IkNtWUR2NU1LNVlVeWlrOEtHTkR3b3c9PSIsInJvbGVzIjoiQ1VTVE9NRVIiLCJpYXQiOjE2ODAyMjY3MDcsImV4cCI6MTY4MDMxMzEwN30.otlY25cTA9MLKRvKsXLGxpC5oj_-67V6W-XEywt6Les");
        headers.add("Content-Type", "application/json;charset=UTF-8");

        //when
        //then
        mockMvc.perform(put("/seller/product")
                .headers(headers)
                .content(objectMapper.writeValueAsString(
                    UpdateProductForm.builder()
                        .productId(1L)
                        .name("name")
                        .description("descripton")
                        .items(List.of(
                            UpdateProductItemForm.builder().build()))
                        .build()
                )))
            .andDo(print())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("name"))
            .andExpect(jsonPath("$.description").value("test"))
            .andExpect(jsonPath("$.items[0].id").value(1))
            .andExpect(jsonPath("$.items[0].name").value("item"))
            .andExpect(jsonPath("$.items[0].price").value(1000))
            .andExpect(jsonPath("$.items[0].count").value(2));
    }

    @Test
    void updateProductItem() throws Exception {
        //given
        given(provider.getUserVo(anyString()))
            .willReturn(new UserVo(1L, "khg5087@email.com"));

        ProductItem productItem = getProductItem();
        given(productItemService.updateProductItem(anyLong(), any()))
            .willReturn(productItem);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCY0ROWnZCMVhCZ3d2QVJBUjhOYUlzOG9Uc2JSR0NCd0VLOGlCRUdsZVFVPSIsImp0aSI6IkNtWUR2NU1LNVlVeWlrOEtHTkR3b3c9PSIsInJvbGVzIjoiQ1VTVE9NRVIiLCJpYXQiOjE2ODAyMjY3MDcsImV4cCI6MTY4MDMxMzEwN30.otlY25cTA9MLKRvKsXLGxpC5oj_-67V6W-XEywt6Les");
        headers.add("Content-Type", "application/json;charset=UTF-8");

        //when
        //then
        mockMvc.perform(put("/seller/product/item")
                .headers(headers)
                .content(objectMapper.writeValueAsString(
                    UpdateProductForm.builder()
                        .productId(1L)
                        .name("name")
                        .description("description")
                        .items(List.of(
                            UpdateProductItemForm.builder().build()))
                        .build()
                )))
            .andDo(print())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("item"))
            .andExpect(jsonPath("$.count").value(2))
            .andExpect(jsonPath("$.price").value(2000));
    }

    @Test
    void deleteProduct() throws Exception {
        //given
        given(provider.getUserVo(anyString()))
            .willReturn(new UserVo(1L, "khg5087@email.com"));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCY0ROWnZCMVhCZ3d2QVJBUjhOYUlzOG9Uc2JSR0NCd0VLOGlCRUdsZVFVPSIsImp0aSI6IkNtWUR2NU1LNVlVeWlrOEtHTkR3b3c9PSIsInJvbGVzIjoiQ1VTVE9NRVIiLCJpYXQiOjE2ODAyMjY3MDcsImV4cCI6MTY4MDMxMzEwN30.otlY25cTA9MLKRvKsXLGxpC5oj_-67V6W-XEywt6Les");
        headers.add("Content-Type", "application/json;charset=UTF-8");

        //when
        //then
        mockMvc.perform(delete("/seller/product?productId=1")
                .headers(headers))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void deleteProductItem() throws Exception {
        //given
        given(provider.getUserVo(anyString()))
            .willReturn(new UserVo(1L, "khg5087@email.com"));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCY0ROWnZCMVhCZ3d2QVJBUjhOYUlzOG9Uc2JSR0NCd0VLOGlCRUdsZVFVPSIsImp0aSI6IkNtWUR2NU1LNVlVeWlrOEtHTkR3b3c9PSIsInJvbGVzIjoiQ1VTVE9NRVIiLCJpYXQiOjE2ODAyMjY3MDcsImV4cCI6MTY4MDMxMzEwN30.otlY25cTA9MLKRvKsXLGxpC5oj_-67V6W-XEywt6Les");
        headers.add("Content-Type", "application/json;charset=UTF-8");

        //when
        //then
        mockMvc.perform(delete("/seller/product/item?productItemId=1")
                .headers(headers))
            .andDo(print())
            .andExpect(status().isOk());
    }

    private ProductItem getProductItem() {
        return ProductItem.builder()
            .id(1L)
            .name("item")
            .count(2L)
            .price(2000L)
            .build();
    }

    private Product getProduct() {
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