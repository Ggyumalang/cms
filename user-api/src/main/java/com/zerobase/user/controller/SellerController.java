package com.zerobase.user.controller;

import com.zerobase.domain.config.JwtAuthenticationProvider;
import com.zerobase.domain.domain.common.UserVo;
import com.zerobase.user.domain.model.Seller;
import com.zerobase.user.domain.seller.SellerDto;
import com.zerobase.user.exception.CustomException;
import com.zerobase.user.service.seller.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.zerobase.user.exception.ErrorCode.NOT_FOUND_USER;

@RestController
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerController {

    private final JwtAuthenticationProvider provider;
    private final SellerService sellerService;
    private final String TOKEN_PREFIX = "Bearer ";

    @GetMapping("/getInfo")
    public ResponseEntity<SellerDto> getInfo(
            @RequestHeader(name = "Authorization") String token
    ) {
        UserVo userVo = provider.getUserVo(token.substring(TOKEN_PREFIX.length()));
        Seller seller = sellerService.findByIdAndEmail(userVo.getId(), userVo.getEmail())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        return ResponseEntity.ok(SellerDto.from(seller));
    }
}
