package com.zerobase.cms.order.application;

import static com.zerobase.cms.order.exception.ErrorCode.ORDER_FAIL_CHECK_CART;
import static com.zerobase.cms.order.exception.ErrorCode.ORDER_FAIL_NO_MONEY;

import com.zerobase.cms.order.client.UserClient;
import com.zerobase.cms.order.client.user.ChangeBalanceForm;
import com.zerobase.cms.order.client.user.CustomerDto;
import com.zerobase.cms.order.domain.model.ProductItem;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.exception.CustomException;
import com.zerobase.cms.order.service.ProductItemService;
import java.util.Objects;
import java.util.stream.LongStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderApplication {

    private final CartApplication cartApplication;
    private final UserClient userClient;
    private final ProductItemService productItemService;

    /**
     * 1번 : 물건들이 전부 주문 가능한 상태인 지 확인 2번 : 가격 변동이 있었는 지에 대해 확인 3번 : 고객의 돈이 충분한 지
     * 4번 : 결제 & 상품의 재고 관리
     *
     * @param token
     * @param cart
     */
    @Transactional
    public Cart order(String token, Cart cart) {
        //1번 : 주문 시 기존 카트를 버림
        //2번 : 선택 주문 시 내가 사지 않은 아이템은 구분해야함.
        Cart orderCart = cartApplication.refreshCart(cart);

        if (orderCart.getMessages().size() > 0) {
            throw new CustomException(ORDER_FAIL_CHECK_CART);
        }

        CustomerDto customerDto = userClient.getCustomerInfo(token).getBody();

        long totalPrice = getTotalPrice(cart);

        if (Objects.requireNonNull(customerDto).getBalance() < totalPrice) {
            throw new CustomException(ORDER_FAIL_NO_MONEY);
        }

        //롤백 계획에 대해서 생각해보아야 한다.
        userClient.changeBalance(
            token
            , ChangeBalanceForm.builder()
                .from("USER")
                .message("ORDER")
                .money(-totalPrice)
                .build()
        );

        for (Cart.Product product : orderCart.getProducts()) {
            for (Cart.ProductItem cartItem : product.getItems()) {
                ProductItem productItem = productItemService.getProductItem(
                    cartItem.getId());
                productItem.setCount(
                    productItem.getCount() - cartItem.getCount());
            }
        }

        cartApplication.clearCart(cart.getCustomerId());

        return orderCart;
    }

    private long getTotalPrice(Cart cart) {
        return cart.getProducts().stream()
            .flatMapToLong(product ->
                (product.getItems().stream()
                    .flatMapToLong(
                        productItem ->
                            LongStream.of(
                                productItem.getPrice() * productItem.getCount())
                    )
                )
            ).sum();
    }

}
