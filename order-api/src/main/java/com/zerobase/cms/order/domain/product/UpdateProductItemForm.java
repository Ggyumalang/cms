package com.zerobase.cms.order.domain.product;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductItemForm {
    private Long productItemId;
    private String name;
    private Long price;
    private Long count;
}
