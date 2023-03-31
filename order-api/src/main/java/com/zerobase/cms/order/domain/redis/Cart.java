package com.zerobase.cms.order.domain.redis;

import com.zerobase.cms.order.domain.product.AddProductCartForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("cart")
public class Cart {
    @Id
    private Long customerId;

    private List<Product> products = new ArrayList<>();
    private List<String> messages = new ArrayList<>();

    public Cart(Long customerId) {
        this.customerId = customerId;
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product {
        private Long id;
        private Long sellerId;
        private String name;
        private String description;
        @Builder.Default
        private List<ProductItem> items = new ArrayList<>();

        public static Product from(AddProductCartForm form){
            return Product.builder()
                    .id(form.getProductId())
                    .sellerId(form.getSellerId())
                    .name(form.getName())
                    .description(form.getDescription())
                    .items(form.getItems().stream()
                            .map(ProductItem::from)
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductItem{
        private Long id;
        private String name;
        private Long count;
        private Long price;

        public static ProductItem from(AddProductCartForm.ProductItem productItem){
            return ProductItem.builder()
                    .id(productItem.getProductItemId())
                    .name(productItem.getName())
                    .count(productItem.getCount())
                    .price(productItem.getPrice())
                    .build();
        }
    }

    public Cart clone() {
        return new Cart(customerId,products,messages);
    }
}