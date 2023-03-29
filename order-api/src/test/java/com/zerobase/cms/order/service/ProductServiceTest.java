package com.zerobase.cms.order.service;

import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.product.AddProductForm;
import com.zerobase.cms.order.domain.product.AddProductItemForm;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void success_add_product() {
        //given 어떤 데이터가 주어졌을 때
        Long sellerId = 1L;
        AddProductForm form = makeProductForm("NIKE AIRFORCE", "THIS IS NIKE", 3);

        given(productRepository.save(any()))
                .willReturn(Product.of(sellerId, form));
        //when 어떤 경우에
        Product p = productService.addProduct(sellerId, form);
        //then 이런 결과가 나온다.
        assertEquals("NIKE AIRFORCE", p.getName());
        assertEquals("THIS IS NIKE", p.getDescription());
        assertEquals(3, p.getProductItems().size());
        assertEquals(1L, p.getProductItems().get(0).getCount());
        assertEquals("NIKE AIRFORCE0", p.getProductItems().get(0).getName());
        assertEquals(10000L, p.getProductItems().get(0).getPrice());

    }

    private static AddProductForm makeProductForm(String name, String description, int itemCount) {
        List<AddProductItemForm> addProductItemForms = new ArrayList<>();

        for (int i = 0; i < itemCount; i++) {
            addProductItemForms.add(makeProductItemForm(1L, name + i));
        }
        return AddProductForm.builder()
                .name(name)
                .description(description)
                .items(addProductItemForms)
                .build();
    }

    private static AddProductItemForm makeProductItemForm(Long productId, String name) {
        return AddProductItemForm.builder()
                .productId(productId)
                .name(name)
                .price(10000L)
                .count(1L)
                .build();
    }
}