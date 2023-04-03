package com.zerobase.cms.order.domain.product;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartProductForm {

    private Long productId;

    private Long sellerId;

    private String name;

    private String description;

    private List<ProductItem> items;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProductItem {

        private Long productItemId;
        private String name;
        private Long count;
        private Long price;
    }
}
