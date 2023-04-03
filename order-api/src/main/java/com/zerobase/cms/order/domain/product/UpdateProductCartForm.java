package com.zerobase.cms.order.domain.product;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateProductCartForm {

    @NotNull
    private List<CartProductForm> products;
}
