package com.zerobase.cms.order.controller;

import com.zerobase.cms.order.domain.jwt.Token;
import com.zerobase.cms.order.domain.product.AddProductForm;
import com.zerobase.cms.order.domain.product.AddProductItemForm;
import com.zerobase.cms.order.domain.product.ProductDto;
import com.zerobase.cms.order.domain.product.ProductItemDto;
import com.zerobase.cms.order.domain.product.UpdateProductForm;
import com.zerobase.cms.order.domain.product.UpdateProductItemForm;
import com.zerobase.cms.order.service.ProductItemService;
import com.zerobase.cms.order.service.ProductService;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/seller/product")
public class SellerProductController {

    private final ProductService productService;
    private final ProductItemService productItemService;
    private final JwtAuthenticationProvider provider;

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(
        @RequestHeader(name = Token.AUTHORIZATION) String token,
        @RequestBody @Valid AddProductForm form
    ) {
        return ResponseEntity.ok(ProductDto.from(
            productService.addProduct(
                provider.getUserVo(token.substring(Token.PREFIX.length()))
                    .getId()
                , form)));
    }

    @PostMapping("/item")
    public ResponseEntity<ProductDto> addProductItem(
        @RequestHeader(name = Token.AUTHORIZATION) String token,
        @RequestBody @Valid AddProductItemForm form
    ) {
        return ResponseEntity.ok(ProductDto.from(
            productItemService.addProductItem(
                provider.getUserVo(
                    token.substring(Token.PREFIX.length())
                ).getId(), form)));
    }

    @PutMapping
    public ResponseEntity<ProductDto> updateProduct(
        @RequestHeader(name = Token.AUTHORIZATION) String token,
        @RequestBody @Valid UpdateProductForm form
    ) {
        return ResponseEntity.ok(ProductDto.from(
            productService.updateProduct(
                provider.getUserVo(
                    token.substring(Token.PREFIX.length())
                ).getId(), form)));
    }

    @PutMapping("/item")
    public ResponseEntity<ProductItemDto> updateProductItem(
        @RequestHeader(name = Token.AUTHORIZATION) String token,
        @RequestBody @Valid UpdateProductItemForm form
    ) {
        return ResponseEntity.ok(ProductItemDto.from(
            productItemService.updateProductItem(
                provider.getUserVo(
                    token.substring(Token.PREFIX.length())
                ).getId(), form)));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProduct(
        @RequestHeader(name = Token.AUTHORIZATION) String token,
        @RequestParam Long productId
    ) {
        productService.deleteProduct(
            provider.getUserVo(token.substring(Token.PREFIX.length())).getId()
            , productId
        );

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/item")
    public ResponseEntity<Void> deleteProductItem(
        @RequestHeader(name = Token.AUTHORIZATION) String token,
        @RequestParam Long productItemId
    ) {
        productItemService.deleteProductItem(
            provider.getUserVo(token.substring(Token.PREFIX.length())).getId()
            , productItemId
        );

        return ResponseEntity.ok().build();
    }
}
