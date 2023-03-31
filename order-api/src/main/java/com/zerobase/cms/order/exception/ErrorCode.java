package com.zerobase.cms.order.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    ORDER_FAIL_CHECK_CART("주문 불가! 장바구니를 확인해 주세요."),
    ORDER_FAIL_NO_MONEY("주문 불가! 잔액 부족입니다."),
    CART_CHANGE_FAIL("장바구니에 추가할 수 없습니다."),
    ITEM_COUNT_NOT_ENOUGH("상품의 수량이 부족합니다."),
    SAME_ITEM_NAME("아이템 명 중복입니다."),

    NOT_FOUND_PRODUCT_ITEM("아이템을 찾을 수 없습니다."),
    NOT_FOUND_PRODUCT("상품을 찾을 수 없습니다.");

    private final String detail;
}
