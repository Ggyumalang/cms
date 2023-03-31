package com.zerobase.cms.order.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.zerobase.cms.order.client.UserClient;
import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.product.AddProductCartForm;
import com.zerobase.cms.order.domain.product.AddProductForm;
import com.zerobase.cms.order.domain.product.AddProductItemForm;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import com.zerobase.cms.order.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CartApplicationTest {

//    @AutoConfigureBefore
//    private JpaAuditingConfiguration jpaAuditingConfiguration;

    @Autowired
    private CartApplication cartApplication;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void add_test_modify() {
        //given 어떤 데이터가 주어졌을 때
        Long customerId = 100L;
        cartApplication.clearCart(customerId);
        Product p = success_add_product();
        Product result = productRepository.findWithProductItemsById(p.getId())
            .get();
        //then 이런 결과가 나온다.

        //when 어떤 경우에
        //then 이런 결과가 나온다.
        assertEquals("NIKE AIRFORCE", p.getName());
        assertEquals("THIS IS NIKE", p.getDescription());
        assertEquals(3, p.getProductItems().size());
        assertEquals(10L, p.getProductItems().get(0).getCount());
        assertEquals("NIKE AIRFORCE0", p.getProductItems().get(0).getName());
        assertEquals(10000L, p.getProductItems().get(0).getPrice());

//        p.getProductItems().get(0).setCount(0L);
//        productRepository.save(p);

        Cart cart = cartApplication.addCart(customerId, makeAddForm(result));

        //데이터가 잘 들어갔는 지 확인하기
        assertEquals(0, cart.getMessages().size());

        cart = cartApplication.getCart(customerId);
        System.out.println("cart >> " + cart.getMessages());
        assertEquals(1, cart.getMessages().size());

    }

    AddProductCartForm makeAddForm(Product p) {
        AddProductCartForm.ProductItem productItem = AddProductCartForm.ProductItem.builder()
            .productItemId(p.getProductItems().get(0).getId())
            .name(p.getProductItems().get(0).getName())
            .count(11L)
            .price(10000L)
            .build();
        return AddProductCartForm.builder()
            .productId(p.getId())
            .sellerId(p.getSellerId())
            .name(p.getName())
            .description(p.getDescription())
            .items(List.of(productItem))
            .build();
    }

    Product success_add_product() {
        //given 어떤 데이터가 주어졌을 때
        Long sellerId = 1L;
        AddProductForm form = makeProductForm("NIKE AIRFORCE", "THIS IS NIKE",
            3);
        //when 어떤 경우에
        return productService.addProduct(sellerId, form);

    }

    private static AddProductForm makeProductForm(String name,
        String description, int itemCount) {
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

    private static AddProductItemForm makeProductItemForm(Long productId,
        String name) {
        return AddProductItemForm.builder()
            .productId(productId)
            .name(name)
            .price(10000L)
            .count(10L)
            .build();
    }

}