package com.zerobase.user.controller;

import com.zerobase.domain.config.JwtAuthenticationProvider;
import com.zerobase.domain.domain.common.UserVo;
import com.zerobase.user.domain.jwt.Token;
import com.zerobase.user.domain.seller.SellerDto;
import com.zerobase.user.service.seller.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerController {

    private final JwtAuthenticationProvider provider;
    private final SellerService sellerService;

    @GetMapping("/getInfo")
    public ResponseEntity<SellerDto> getInfo(
        @RequestHeader(name = Token.AUTHORIZATION) String token
    ) {
        UserVo userVo = provider.getUserVo(
            token.substring(Token.PREFIX.length()));

        return ResponseEntity.ok(sellerService.findByIdAndEmail(
            userVo.getId(), userVo.getEmail()
        ));
    }
}
