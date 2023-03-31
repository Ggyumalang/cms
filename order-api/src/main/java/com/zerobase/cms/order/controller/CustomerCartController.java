package com.zerobase.cms.order.controller;

import com.zerobase.cms.order.application.CartApplication;
import com.zerobase.cms.order.domain.jwt.Token;
import com.zerobase.cms.order.domain.product.AddProductCartForm;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/customer/cart")
public class CustomerCartController {

    private final CartApplication cartApplication;

    private final JwtAuthenticationProvider provider;

    @PostMapping
    public ResponseEntity<Cart> addCart(
            @RequestHeader(name = Token.AUTHORIZATION) String token,
            @RequestBody AddProductCartForm form
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
}
