package com.zerobase.cms.order.service;

import com.zerobase.cms.order.client.RedisClient;
import com.zerobase.cms.order.domain.product.AddProductCartForm;
import com.zerobase.cms.order.domain.redis.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CartService {

    private final RedisClient redisClient;

    public Cart getCart(Long customerId) {
        return redisClient.get(customerId, Cart.class);
    }

    public Cart addCart(Long customerId, AddProductCartForm form) {

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
            Cart.Product redisProduct = optionalProduct.get();
            //requested
            List<Cart.ProductItem> items = form.getItems().stream()
                                                        .map(Cart.ProductItem::from)
                                                        .collect(Collectors.toList());

            Map<Long, Cart.ProductItem> redisItemMap = redisProduct.getItems().stream()
                    .collect(Collectors.toMap(Cart.ProductItem::getId, it->it));

            if(!redisProduct.getName().equals(form.getName())){
                cart.addMessage(redisProduct.getName() + "의 정보가 변경되었습니다."
                + "확인부탁드립니다.");
            }

            for(Cart.ProductItem item : items) {
                Cart.ProductItem redisItem = redisItemMap.get(item.getId());

                if(Objects.isNull(redisItem)){
                    //happyCase 그냥 넣으면 됨
                    redisProduct.getItems().add(item);
                }else {
                    if(!redisItem.getPrice().equals(item.getPrice())){
                        cart.addMessage(redisProduct.getName()
                                + " "
                                + item.getName()
                                + "의 가격이 변경되었습니다."
                                + "확인부탁드립니다.");
                    }
                    redisItem.setCount(item.getCount() + redisItem.getCount());
                }
            }
        } else {
            Cart.Product product = Cart.Product.from(form);
            cart.getProducts().add(product);
        }
        redisClient.put(customerId, cart);
        return cart;
    }
}
