package com.zerobase.cms.order.service;

import com.zerobase.cms.order.client.RedisClient;
import com.zerobase.cms.order.domain.product.CartProductForm;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.domain.redis.Cart.Product;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartService {

    private final RedisClient redisClient;

    public Cart getCart(Long customerId) {
        Cart cart = redisClient.get(customerId, Cart.class);
        return cart != null ? cart : new Cart();
    }

    public Cart putCart(Long customerId, Cart cart) {
        redisClient.put(customerId, cart);
        return cart;
    }

    public void addCart(Long customerId, CartProductForm form) {

        Cart cart = redisClient.get(customerId, Cart.class);

        if (Objects.isNull(cart)) {
            cart = new Cart();
            cart.setCustomerId(customerId);
        }

        Optional<Cart.Product> optionalProduct = cart.getProducts().stream()
            .filter(product -> product.getId().equals(form.getProductId()))
            .findFirst();

        // 이전에 같은 상품이 있는 지?
        if (optionalProduct.isPresent()) {
            checkedAddableAndAdd(form, cart, optionalProduct.get());
        } else {
            Cart.Product product = Cart.Product.from(form);
            cart.getProducts().add(product);
        }
        redisClient.put(customerId, cart);
    }

    /**
     * 해당 상품이 장바구니에 추가가능한 지 확인 후 추가하는 로직
     *
     * @param form         (요청)
     * @param cart         (장바구니)
     * @param redisProduct (장바구니에 담긴 물건)
     */
    private void checkedAddableAndAdd(
        CartProductForm form
        , Cart cart
        , Product redisProduct
    ) {

        List<Cart.ProductItem> items = form.getItems().stream()
            .map(Cart.ProductItem::from)
            .collect(Collectors.toList());

        Map<Long, Cart.ProductItem> redisItemMap = redisProduct.getItems()
            .stream()
            .collect(Collectors.toMap(Cart.ProductItem::getId, it -> it));

        if (!redisProduct.getName().equals(form.getName())) {
            cart.addMessage(redisProduct.getName()
                + "의 상품명이 변경되었습니다."
                + "확인부탁드립니다.");
        }

        for (Cart.ProductItem item : items) {
            Cart.ProductItem redisItem = redisItemMap.get(item.getId());

            if (Objects.isNull(redisItem)) {
                //happyCase 그냥 넣으면 됨
                redisProduct.getItems().add(item);
            } else {
                if (!redisItem.getPrice().equals(item.getPrice())) {
                    cart.addMessage(redisProduct.getName()
                        + " "
                        + item.getName()
                        + "의 가격이 변경되었습니다."
                        + "확인부탁드립니다.");
                }
                redisItem.setCount(item.getCount() + redisItem.getCount());
            }
        }
    }

}
