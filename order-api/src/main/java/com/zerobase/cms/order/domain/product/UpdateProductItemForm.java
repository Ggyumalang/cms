package com.zerobase.cms.order.domain.product;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductItemForm {

    @NotNull
    @Min(1)
    private Long productItemId;

    @NotBlank
    private String name;

    @NotNull
    @Min(1)
    private Long price;

    @NotNull
    private Long count;
}
