package com.zerobase.user.controller;

import com.zerobase.domain.config.JwtAuthenticationProvider;
import com.zerobase.domain.domain.common.UserVo;
import com.zerobase.user.domain.customer.ChangeBalanceForm;
import com.zerobase.user.domain.customer.CustomerDto;
import com.zerobase.user.domain.model.Customer;
import com.zerobase.user.exception.CustomException;
import com.zerobase.user.service.customer.CustomerBalanceService;
import com.zerobase.user.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.zerobase.user.exception.ErrorCode.NOT_FOUND_USER;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final JwtAuthenticationProvider provider;
    private final CustomerService customerService;
    private final CustomerBalanceService customerBalanceService;
    private final String TOKEN_PREFIX = "Bearer ";

    @GetMapping("/getInfo")
    public ResponseEntity<CustomerDto> getInfo(
            @RequestHeader(name = "Authorization") String token
    ) {
        UserVo userVo = provider.getUserVo(token.substring(TOKEN_PREFIX.length()));
        Customer customer = customerService.findByIdAndEmail(userVo.getId(), userVo.getEmail())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        return ResponseEntity.ok(CustomerDto.from(customer));
    }

    @PostMapping("/balance")
    public ResponseEntity<Long> changeBalance(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody ChangeBalanceForm form
    ){
        UserVo vo = provider.getUserVo(token.substring(TOKEN_PREFIX.length()));
        return ResponseEntity.ok(customerBalanceService.changeBalance(vo.getId(), form).getCurrentMoney());
    }
}
