package com.zerobase.cms.order.domain.model;

import static com.zerobase.cms.order.exception.ErrorCode.NOT_FOUND_PRODUCT_ITEM;

import com.zerobase.cms.order.domain.product.AddProductForm;
import com.zerobase.cms.order.domain.product.UpdateProductForm;
import com.zerobase.cms.order.domain.product.UpdateProductItemForm;
import com.zerobase.cms.order.exception.CustomException;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Audited
@AuditOverride(forClass = BaseEntity.class)
@Entity
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sellerId;

    private String name;

    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    @Builder.Default
    private List<ProductItem> productItems = new ArrayList<>();

    public static Product of(Long sellerId, AddProductForm form) {
        return Product.builder()
                .sellerId(sellerId)
                .name(form.getName())
                .description(form.getDescription())
                .productItems(form.getItems().stream()
                        .map(pi -> ProductItem.of(sellerId, pi))
                        .collect(Collectors.toList()))
                .build();
    }

    public void updateProductInfo(UpdateProductForm form) {

        this.name = form.getName();
        this.description = form.getDescription();

        for (UpdateProductItemForm itemForm : form.getItems()) {
            ProductItem item = this.productItems.stream()
                .filter(pi -> pi.getId().equals(itemForm.getProductItemId())
                            && pi.getName().equals(itemForm.getName()))
                .findFirst()
                .orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT_ITEM));

            item.setName(itemForm.getName());
            item.setPrice(itemForm.getPrice());
            item.setCount(itemForm.getCount());
        }
    }

}
