package com.zerobase.cms.order.domain.product;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddProductCartForm {

    @NotNull
    @Min(1)
    private Long productId;

    @NotNull
    @Min(1)
    private Long sellerId;

    @NotBlank
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
