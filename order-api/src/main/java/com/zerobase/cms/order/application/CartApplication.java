package com.zerobase.cms.order.application;

import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.model.ProductItem;
import com.zerobase.cms.order.domain.product.AddProductCartForm;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.exception.CustomException;
import com.zerobase.cms.order.service.CartService;
import com.zerobase.cms.order.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.zerobase.cms.order.exception.ErrorCode.ITEM_COUNT_NOT_ENOUGH;
import static com.zerobase.cms.order.exception.ErrorCode.NOT_FOUND_PRODUCT;

@RequiredArgsConstructor
@Service
public class CartApplication {

    private final ProductSearchService productSearchService;

    private final CartService cartService;

    public Cart addCart(Long customerId , AddProductCartForm form) {
        Product product = productSearchService.getByProductId(form.getProductId());
        if(Objects.isNull(product)){
            throw new CustomException(NOT_FOUND_PRODUCT);
        }
        Cart cart = cartService.getCart(customerId);

        if(!Objects.isNull(cart) && !addAble(cart, product, form)){
            throw new CustomException(ITEM_COUNT_NOT_ENOUGH);
        }

        return cartService.addCart(customerId, form);
    }

    /**
     * Product Item 의 재고가 장바구니에 현재 담긴 값 + 새로 담는 값보다 작다면 false
     * @param cart
     * @param product
     * @param form
     * @return
     */
    private boolean addAble(Cart cart, Product product, AddProductCartForm form) {
        Cart.Product cartProduct = cart.getProducts().stream()
                .filter(p -> p.getId().equals(form.getProductId()))
                .findFirst()
                .orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));

        Map<Long,Long> cartItemCountMap = cartProduct.getItems().stream()
                .collect(Collectors.toMap(
                        Cart.ProductItem::getId
                        , Cart.ProductItem::getCount)
                );

        Map<Long,Long> currentItemCountMap = product.getProductItems().stream()
                .collect(Collectors.toMap(
                        ProductItem::getId
                        , ProductItem::getCount)
                );

        return form.getItems().stream().noneMatch(
            formItem -> {
                Long cartCount = cartItemCountMap.get(formItem.getProductItemId());
                Long currentCount = currentItemCountMap.get(formItem.getCount());
                return currentCount < cartCount + formItem.getCount();
            });
    }
}
