package com.zerobase.cms.order.domain.model;

import com.zerobase.cms.order.domain.product.AddProductItemForm;
import com.zerobase.cms.order.domain.product.UpdateProductItemForm;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@AuditOverride(forClass = BaseEntity.class)
@Entity
public class ProductItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sellerId;

    @Audited
    private String name;

    @Audited
    private Long price;

    private Long count;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    public static ProductItem of(Long sellerId, AddProductItemForm form) {
        return ProductItem.builder()
                .sellerId(sellerId)
                .name(form.getName())
                .price(form.getPrice())
                .count(form.getCount())
                .build();
    }

    public void updateProductItemInfo(UpdateProductItemForm form) {
        this.name = form.getName();
        this.count = form.getCount();
        this.price = form.getPrice();
    }
}
