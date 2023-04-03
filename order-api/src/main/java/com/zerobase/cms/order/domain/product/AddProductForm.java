package com.zerobase.cms.order.domain.product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddProductForm {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private List<AddProductItemForm> items;

}
