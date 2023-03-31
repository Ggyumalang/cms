package com.zerobase.cms.order.application;

import static com.zerobase.cms.order.exception.ErrorCode.ITEM_COUNT_NOT_ENOUGH;
import static com.zerobase.cms.order.exception.ErrorCode.NOT_FOUND_PRODUCT;

import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.model.ProductItem;
import com.zerobase.cms.order.domain.product.AddProductCartForm;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.exception.CustomException;
import com.zerobase.cms.order.service.CartService;
import com.zerobase.cms.order.service.ProductSearchService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartApplication {

    private final ProductSearchService productSearchService;

    private final CartService cartService;

    public Cart addCart(Long customerId, AddProductCartForm form) {
        Product product = productSearchService.getByProductId(
            form.getProductId());
        if (Objects.isNull(product)) {
            throw new CustomException(NOT_FOUND_PRODUCT);
        }
        Cart cart = cartService.getCart(customerId);

        if (!Objects.isNull(cart) && !addAble(cart, product, form)) {
            throw new CustomException(ITEM_COUNT_NOT_ENOUGH);
        }

        cartService.addCart(customerId, form);

        return getCart(customerId);
    }

    public Cart updateCart(Long customerId, Cart cart) {
        cartService.putCart(customerId, cart);
        return getCart(customerId);
    }


    public Cart getCart(Long customerId) {
        Cart cart = refreshCart(cartService.getCart(customerId));

        if (Objects.isNull(cart)) {
            cart = new Cart();
            cart.setCustomerId(customerId);
            return cart;
        }

        Cart returnCart = new Cart();
        returnCart.setCustomerId(customerId);
        returnCart.setProducts(cart.getProducts());
        returnCart.setMessages(cart.getMessages());

        //2. 메시지를 보내고 난 다음에는 제거한다.
        cart.setMessages(new ArrayList<>());
        cartService.putCart(customerId, cart);

        return returnCart;
    }

    public void clearCart(Long customerId) {
        cartService.putCart(customerId, null);
    }

    /**
     * 1. 상품이나 상품의 아이템의 정보, 가격, 수량이 변경되었는 지 체크하고 그에 맞는 메세지를 제공한다. 2. 상품의 수량, 가격을
     * 우리가 임의로 변경한다.
     *
     * @param cart
     * @return cart
     */
    protected Cart refreshCart(Cart cart) {

        if (Objects.isNull(cart.getCustomerId())) {
            return null;
        }

        Map<Long, Product> productMap = productSearchService.getListByProductIds(
                cart.getProducts().stream()
                    .map(Cart.Product::getId)
                    .collect(Collectors.toList()))
            .stream()
            .collect(Collectors.toMap(Product::getId, product -> product));

        for (int i = 0; i < cart.getProducts().size(); i++) {
            Cart.Product cartProduct = cart.getProducts().get(i);

            Product product = productMap.get(cartProduct.getId());
            if (Objects.isNull(product)) {
                cart.getProducts().remove(cartProduct);
                i--;
                cart.addMessage(cartProduct.getName() + " 상품이 삭제되었습니다.");
                continue;
            }
            Map<Long, ProductItem> productItemMap = product.getProductItems()
                .stream()
                .collect(Collectors.toMap(ProductItem::getId,
                    productItem -> productItem));

            //각각 케이스 별로 에러를 쪼개고, 에러가 정상 출력되는 지 확인할 것.
            List<String> tmpMessages = new ArrayList<>();

            for (int j = 0; j < cartProduct.getItems().size(); j++) {
                Cart.ProductItem cartProductItem = cartProduct.getItems()
                    .get(j);

                ProductItem productItem = productItemMap.get(
                    cartProductItem.getId());

                if (Objects.isNull(productItem)) {
                    cartProduct.getItems().remove(cartProductItem);
                    j--;
                    tmpMessages.add(
                        cartProductItem.getName() + " 옵션이 삭제되었습니다.");
                    continue;
                }

                boolean isPriceChanged = false, isCountNotEnough = false;

                if (!cartProductItem.getPrice()
                    .equals(productItem.getPrice())) {
                    isPriceChanged = true;
                    cartProductItem.setPrice(productItem.getPrice());
                }

                if (cartProductItem.getCount() > productItem.getCount()) {
                    isCountNotEnough = true;
                    cartProductItem.setCount(productItem.getCount());
                }

                if (isPriceChanged && isCountNotEnough) {
                    // message 1
                    tmpMessages.add(cartProductItem.getName()
                        + " 가격이 변경되었고 수량이 부족하여 구매 가능한 최대 수량으로 변경되었습니다.");
                } else if (isPriceChanged) {
                    // message 2
                    tmpMessages.add(
                        cartProductItem.getName() + " 가격이 변경되었습니다.");
                } else if (isCountNotEnough) {
                    // message 3
                    tmpMessages.add(cartProductItem.getName()
                        + " 수량이 부족하여 구매 가능한 최대 수량으로 변경되었습니다.");
                }
            }
            if (cartProduct.getItems().size() == 0) {
                cart.getProducts().remove(cartProduct);
                i--;
                cart.addMessage(
                    cartProduct.getName() + " 상품의 옵션이 모두 없어져 구매가 불가능합니다.");
            } else if (tmpMessages.size() > 0) {
                StringBuilder builder = new StringBuilder();
                builder.append(cartProduct.getName())
                    .append(" 상품의 변동 사항 : ");

                for (String message : tmpMessages) {
                    builder.append(message);
                    builder.append(", ");
                }
                cart.addMessage(builder.toString());
            }
        }

        return cartService.putCart(cart.getCustomerId(), cart);
    }

    /**
     * Product Item 의 재고가 장바구니에 현재 담긴 값 + 새로 담는 값보다 작다면 false
     *
     * @param cart
     * @param product
     * @param form
     * @return
     */
    private boolean addAble(Cart cart, Product product,
        AddProductCartForm form) {

        Cart.Product cartProduct = cart.getProducts().stream()
            .filter(p -> p.getId().equals(form.getProductId()))
            .findFirst()
            .orElse(Cart.Product.builder()
                .id(product.getId())
                .items(Collections.emptyList())
                .build());

        Map<Long, Long> cartItemCountMap = cartProduct.getItems().stream()
            .collect(Collectors.toMap(
                Cart.ProductItem::getId
                , Cart.ProductItem::getCount)
            );

        Map<Long, Long> currentItemCountMap = product.getProductItems().stream()
            .collect(Collectors.toMap(
                ProductItem::getId
                , ProductItem::getCount)
            );

        return form.getItems().stream().noneMatch(
            formItem -> {
                Long cartCount = cartItemCountMap.getOrDefault(
                    formItem.getProductItemId(), 0L);
                Long currentCount = currentItemCountMap.getOrDefault(
                    formItem.getProductItemId(), 0L);
                Long formCount = formItem.getCount();
                return currentCount < cartCount + formCount;
            });
    }
}
