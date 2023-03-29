package com.zerobase.cms.order.controller;

import com.zerobase.cms.order.domain.product.AddProductForm;
import com.zerobase.cms.order.domain.product.AddProductItemForm;
import com.zerobase.cms.order.domain.product.ProductDto;
import com.zerobase.cms.order.service.ProductItemService;
import com.zerobase.cms.order.service.ProductService;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/seller/product")
public class SellerProductController {

    private final ProductService productService;
    private final ProductItemService productItemService;
    private final JwtAuthenticationProvider provider;

    private final String TOKEN_PREFIX = "Bearer ";

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody AddProductForm form
    ) {
        return ResponseEntity.ok(ProductDto.from(
                productService.addProduct(
                        provider.getUserVo(
                                token.substring(TOKEN_PREFIX.length())
                        ).getId(), form)));
    }

    @PostMapping("/item")
    public ResponseEntity<ProductDto> addProductItem(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody AddProductItemForm form
    ) {
        return ResponseEntity.ok(ProductDto.from(
                productItemService.addProductItem(
                        provider.getUserVo(
                                token.substring(TOKEN_PREFIX.length())
                        ).getId(), form)));
    }
}
