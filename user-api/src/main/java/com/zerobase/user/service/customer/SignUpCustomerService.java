package com.zerobase.user.service.customer;

import static com.zerobase.user.exception.ErrorCode.ALREADY_VERIFIED_USER;
import static com.zerobase.user.exception.ErrorCode.EXPIRED_VERIFICATION;
import static com.zerobase.user.exception.ErrorCode.NOT_FOUND_USER;
import static com.zerobase.user.exception.ErrorCode.WRONG_VERIFICATION_CODE;

import com.zerobase.user.domain.SignUpForm;
import com.zerobase.user.domain.model.Customer;
import com.zerobase.user.domain.repository.CustomerRepository;
import com.zerobase.user.exception.CustomException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignUpCustomerService {

    private final CustomerRepository customerRepository;

    public Customer signUp(SignUpForm form) {
        return customerRepository.save(Customer.from(form));
    }

    public boolean isEmailExist(String email) {
        return customerRepository.findByEmail(email.toLowerCase(Locale.ROOT))
            .isPresent();
    }

    @Transactional
    public void verifyEmail(String email, String code) {
        Customer customer = customerRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        if (customer.isVerify()) {
            throw new CustomException(ALREADY_VERIFIED_USER);
        }

        if (!code.equals(customer.getVerificationCode())) {
            throw new CustomException(WRONG_VERIFICATION_CODE);
        }

        if (LocalDateTime.now().isAfter(customer.getVerifyExpiredAt())) {
            throw new CustomException(EXPIRED_VERIFICATION);
        }

        customer.setVerify(true);
    }

    @Transactional
    public void changeCustomerValidateEmail(Long customerId,
        String verificationCode) {
        Optional<Customer> optionalCustomer = customerRepository.findById(
            customerId);

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            customer.setVerificationCode(verificationCode);
            customer.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));
        }

        throw new CustomException(NOT_FOUND_USER);
    }
}
