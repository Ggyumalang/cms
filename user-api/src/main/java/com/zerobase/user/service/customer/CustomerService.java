package com.zerobase.user.service.customer;

import static com.zerobase.user.exception.ErrorCode.NOT_FOUND_USER;

import com.zerobase.user.domain.customer.CustomerDto;
import com.zerobase.user.domain.model.Customer;
import com.zerobase.user.domain.repository.CustomerRepository;
import com.zerobase.user.exception.CustomException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerDto findByIdAndEmail(Long id, String email) {
        return CustomerDto.from(customerRepository.findById(id)
            .stream().filter(customer -> email.equals(customer.getEmail()))
            .findFirst()
            .orElseThrow(() -> new CustomException(NOT_FOUND_USER)));
    }

    public Optional<Customer> findValidCustomer(String email, String password) {
        return customerRepository.findByEmail(email)
            .stream()
            .filter(
                customer -> password.equals(customer.getPassword())
                    && customer.isVerify()
            )
            .findFirst();
    }
}
