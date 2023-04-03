package com.zerobase.user.controller;

import com.zerobase.domain.config.JwtAuthenticationProvider;
import com.zerobase.domain.domain.common.UserVo;
import com.zerobase.user.domain.customer.ChangeBalanceForm;
import com.zerobase.user.domain.customer.CustomerDto;
import com.zerobase.user.domain.jwt.Token;
import com.zerobase.user.service.customer.CustomerBalanceService;
import com.zerobase.user.service.customer.CustomerService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final JwtAuthenticationProvider provider;
    private final CustomerService customerService;
    private final CustomerBalanceService customerBalanceService;

    @GetMapping("/getInfo")
    public ResponseEntity<CustomerDto> getInfo(
        @RequestHeader(name = Token.AUTHORIZATION) String token
    ) {
        UserVo userVo = provider.getUserVo(
            token.substring(Token.PREFIX.length()));
        return ResponseEntity.ok(
            customerService.findByIdAndEmail(userVo.getId(), userVo.getEmail())
        );
    }

    @PostMapping("/balance")
    public ResponseEntity<Long> changeBalance(
        @RequestHeader(name = Token.AUTHORIZATION) String token,
        @RequestBody @Valid ChangeBalanceForm form
    ) {
        UserVo vo = provider.getUserVo(token.substring(Token.PREFIX.length()));
        return ResponseEntity.ok(
            customerBalanceService.changeBalance(vo.getId(), form)
                .getCurrentMoney());
    }
}
