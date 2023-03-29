package com.zerobase.cms.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.cms.order.config.QueryDslConfig;
import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.model.ProductItem;
import com.zerobase.cms.order.domain.product.AddProductForm;
import com.zerobase.cms.order.domain.product.AddProductItemForm;
import com.zerobase.cms.order.domain.repository.ProductItemRepository;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import com.zerobase.cms.order.service.ProductItemService;
import com.zerobase.cms.order.service.ProductService;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(SellerProductController.class)
class SellerProductControllerTest {

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductItemService productItemService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ProductItemRepository productItemRepository;

    @MockBean
    private JwtAuthenticationProvider provider;

    @MockBean
    private QueryDslConfig queryDslConfig;

    @MockBean
    private JPAQueryFactory jpaQueryFactory;

    @MockBean(JpaMetamodelMappingContext.class)

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addProduct() throws Exception {
        //given
        Product product = Product.builder()
                .id(1L)
                .sellerId(1L)
                .name("name")
                .description("test")
                .productItems(List.of(
                        ProductItem.builder()
                                .name("item")
                                .count(2L)
                                .price(1000L)
                                .sellerId(1L)
                                .build()
                ))
                .build();
        given(productService.addProduct(anyLong(), any()))
                .willReturn(product);
        //when
        //then
        mockMvc.perform(post("/seller/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                AddProductForm.builder()
                                        .name("name")
                                        .description("descripton")
                                        .items(List.of(AddProductItemForm.builder().build()))
                                        .build()
                        )))
                .andDo(print())
                .andExpect(jsonPath("$.name").value("name"));
    }

    @Test
    void addProductItem() {
        //given
        //when
        //then
    }

    @Test
    void updateProduct() {
        //given
        //when
        //then
    }

    @Test
    void updateProductItem() {
        //given
        //when
        //then
    }

    @Test
    void deleteProduct() {
        //given
        //when
        //then
    }

    @Test
    void deleteProductItem() {
        //given
        //when
        //then
    }
}