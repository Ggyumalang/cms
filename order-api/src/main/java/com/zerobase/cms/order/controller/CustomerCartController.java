package com.zerobase.cms.order.controller;

import com.zerobase.cms.order.application.CartApplication;
import com.zerobase.cms.order.application.OrderApplication;
import com.zerobase.cms.order.domain.jwt.Token;
import com.zerobase.cms.order.domain.product.AddProductCartForm;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/customer/cart")
public class CustomerCartController {

    private final CartApplication cartApplication;
    private final OrderApplication orderApplication;
    private final JwtAuthenticationProvider provider;

    @PostMapping
    public ResponseEntity<Cart> addCart(
        @RequestHeader(name = Token.AUTHORIZATION) String token,
        @RequestBody @Valid AddProductCartForm form
    ) {
        return ResponseEntity.ok(cartApplication.addCart(
            provider.getUserVo(token.substring(Token.PREFIX.length())).getId()
            , form)
        );
    }

    @GetMapping
    public ResponseEntity<Cart> showCart(
        @RequestHeader(name = Token.AUTHORIZATION) String token
    ) {
        return ResponseEntity.ok(cartApplication.getCart(provider.getUserVo(
            token.substring(Token.PREFIX.length())).getId()));
    }

    @PutMapping
    public ResponseEntity<Cart> updateCart(
        @RequestHeader(name = Token.AUTHORIZATION) String token
        , @RequestBody Cart cart
    ) {
        return ResponseEntity.ok(cartApplication.updateCart(provider.getUserVo(
            token.substring(Token.PREFIX.length())).getId(), cart));
    }

    @PostMapping("/order")
    public ResponseEntity<Cart> order(
        @RequestHeader(name = Token.AUTHORIZATION) String token,
        @RequestBody Cart cart
    ) {
        return ResponseEntity.ok(orderApplication.order(
            token, cart)
        );
    }
}
