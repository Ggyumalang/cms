package com.zerobase.user.controller;

import com.zerobase.user.application.SignInApplication;
import com.zerobase.user.domain.SignInForm;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/signin")
public class SignInController {

    private final SignInApplication signInApplication;

    @PostMapping("/customer")
    public ResponseEntity<String> signInCustomer(
        @RequestBody @Valid SignInForm form
    ) {
        return ResponseEntity.ok(signInApplication.customerLoginToken(form));
    }

    @PostMapping("/seller")
    public ResponseEntity<String> signInSeller(
        @RequestBody @Valid SignInForm form
    ) {
        return ResponseEntity.ok(signInApplication.sellerLoginToken(form));
    }

}
