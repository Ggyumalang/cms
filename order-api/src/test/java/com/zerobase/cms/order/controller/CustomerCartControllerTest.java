package com.zerobase.cms.order.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.cms.order.application.CartApplication;
import com.zerobase.cms.order.application.OrderApplication;
import com.zerobase.cms.order.domain.product.AddProductCartForm;
import com.zerobase.cms.order.domain.product.AddProductCartForm.ProductItem;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import com.zerobase.domain.domain.common.UserVo;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

//@Import(config.class)
@WebMvcTest(CustomerCartController.class)
class CustomerCartControllerTest {

    @MockBean
    private CartApplication cartApplication;

    @MockBean
    private OrderApplication orderApplication;

    @MockBean
    private JwtAuthenticationProvider provider;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addCart() throws Exception {
        //given
        given(provider.getUserVo(anyString()))
            .willReturn(new UserVo(1L, "khg5087@email.com"));

        Cart cart = getCart();
        given(cartApplication.addCart(anyLong(), any()))
            .willReturn(cart);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCY0ROWnZCMVhCZ3d2QVJBUjhOYUlzOG9Uc2JSR0NCd0VLOGlCRUdsZVFVPSIsImp0aSI6IkNtWUR2NU1LNVlVeWlrOEtHTkR3b3c9PSIsInJvbGVzIjoiQ1VTVE9NRVIiLCJpYXQiOjE2ODAyMjY3MDcsImV4cCI6MTY4MDMxMzEwN30.otlY25cTA9MLKRvKsXLGxpC5oj_-67V6W-XEywt6Les");
        headers.add("Content-Type", "application/json;charset=UTF-8");
        //when
        //then
        mockMvc.perform(post("/customer/cart")
                .headers(headers)
                .content(objectMapper.writeValueAsString(
                    AddProductCartForm.builder()
                        .name("name")
                        .description("descripton")
                        .items(List.of(
                            ProductItem.builder().build()))
                        .build()
                )))
            .andDo(print())
            .andExpect(jsonPath("$.customerId").value(1))
            .andExpect(jsonPath("$.products[0].id").value(1))
            .andExpect(jsonPath("$.products[0].sellerId").value(1))
            .andExpect(jsonPath("$.products[0].name").value("name"))
            .andExpect(jsonPath("$.products[0].description").value("test"))
            .andExpect(jsonPath("$.products[0].items[0].id").value(1))
            .andExpect(jsonPath("$.products[0].items[0].name").value("item"))
            .andExpect(jsonPath("$.products[0].items[0].count").value(2))
            .andExpect(jsonPath("$.products[0].items[0].price").value(1000))
            .andExpect(jsonPath("$.messages[0]").value("error"));
    }

    @Test
    void showCart() throws Exception {
        //given
        given(provider.getUserVo(anyString()))
            .willReturn(new UserVo(1L, "khg5087@email.com"));

        Cart cart = getCart();
        given(cartApplication.getCart(anyLong()))
            .willReturn(cart);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCY0ROWnZCMVhCZ3d2QVJBUjhOYUlzOG9Uc2JSR0NCd0VLOGlCRUdsZVFVPSIsImp0aSI6IkNtWUR2NU1LNVlVeWlrOEtHTkR3b3c9PSIsInJvbGVzIjoiQ1VTVE9NRVIiLCJpYXQiOjE2ODAyMjY3MDcsImV4cCI6MTY4MDMxMzEwN30.otlY25cTA9MLKRvKsXLGxpC5oj_-67V6W-XEywt6Les");
        headers.add("Content-Type", "application/json;charset=UTF-8");
        //when
        //then
        mockMvc.perform(get("/customer/cart")
                .headers(headers))
            .andDo(print())
            .andExpect(jsonPath("$.customerId").value(1))
            .andExpect(jsonPath("$.products[0].id").value(1))
            .andExpect(jsonPath("$.products[0].sellerId").value(1))
            .andExpect(jsonPath("$.products[0].name").value("name"))
            .andExpect(jsonPath("$.products[0].description").value("test"))
            .andExpect(jsonPath("$.products[0].items[0].id").value(1))
            .andExpect(jsonPath("$.products[0].items[0].name").value("item"))
            .andExpect(jsonPath("$.products[0].items[0].count").value(2))
            .andExpect(jsonPath("$.products[0].items[0].price").value(1000))
            .andExpect(jsonPath("$.messages[0]").value("error"));
    }

    @Test
    void updateCart() throws Exception {
        //given
        given(provider.getUserVo(anyString()))
            .willReturn(new UserVo(1L, "khg5087@email.com"));

        Cart cart = getCart();
        given(cartApplication.updateCart(anyLong(), any()))
            .willReturn(cart);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCY0ROWnZCMVhCZ3d2QVJBUjhOYUlzOG9Uc2JSR0NCd0VLOGlCRUdsZVFVPSIsImp0aSI6IkNtWUR2NU1LNVlVeWlrOEtHTkR3b3c9PSIsInJvbGVzIjoiQ1VTVE9NRVIiLCJpYXQiOjE2ODAyMjY3MDcsImV4cCI6MTY4MDMxMzEwN30.otlY25cTA9MLKRvKsXLGxpC5oj_-67V6W-XEywt6Les");
        headers.add("Content-Type", "application/json;charset=UTF-8");
        //when
        //then
        mockMvc.perform(put("/customer/cart")
                .headers(headers)
                .content(objectMapper.writeValueAsString(
                    Cart.builder()
                        .customerId(1L)
                        .build()
                )))
            .andDo(print())
            .andExpect(jsonPath("$.customerId").value(1))
            .andExpect(jsonPath("$.products[0].id").value(1))
            .andExpect(jsonPath("$.products[0].sellerId").value(1))
            .andExpect(jsonPath("$.products[0].name").value("name"))
            .andExpect(jsonPath("$.products[0].description").value("test"))
            .andExpect(jsonPath("$.products[0].items[0].id").value(1))
            .andExpect(jsonPath("$.products[0].items[0].name").value("item"))
            .andExpect(jsonPath("$.products[0].items[0].count").value(2))
            .andExpect(jsonPath("$.products[0].items[0].price").value(1000))
            .andExpect(jsonPath("$.messages[0]").value("error"));
    }

    @Test
    void order() throws Exception {
        //given
        given(provider.getUserVo(anyString()))
            .willReturn(new UserVo(1L, "khg5087@email.com"));

        Cart cart = getCart();
        given(cartApplication.refreshCart(any()))
            .willReturn(cart);

        given(orderApplication.order(anyString(), any()))
            .willReturn(cart);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCY0ROWnZCMVhCZ3d2QVJBUjhOYUlzOG9Uc2JSR0NCd0VLOGlCRUdsZVFVPSIsImp0aSI6IkNtWUR2NU1LNVlVeWlrOEtHTkR3b3c9PSIsInJvbGVzIjoiQ1VTVE9NRVIiLCJpYXQiOjE2ODAyMjY3MDcsImV4cCI6MTY4MDMxMzEwN30.otlY25cTA9MLKRvKsXLGxpC5oj_-67V6W-XEywt6Les");
        headers.add("Content-Type", "application/json;charset=UTF-8");
        //when
        //then
        mockMvc.perform(post("/customer/cart")
                .headers(headers)
                .content(objectMapper.writeValueAsString(
                    Cart.builder()
                        .customerId(1L)
                        .build()
                )))
            .andDo(print())
            .andExpect(status().isOk());
    }

    private Cart getCart() {
        return Cart.builder()
            .customerId(1L)
            .products(List.of(getCartProduct()))
            .messages(List.of("error"))
            .build();
    }

    private Cart.ProductItem getCartProductItem() {
        return Cart.ProductItem.builder()
            .id(1L)
            .name("item")
            .count(2L)
            .price(1000L)
            .build();
    }

    private Cart.Product getCartProduct() {
        return Cart.Product.builder()
            .id(1L)
            .sellerId(1L)
            .name("name")
            .description("test")
            .items(List.of(
                getCartProductItem()
            ))
            .build();
    }
}