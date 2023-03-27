package com.zerobase.user.service;

import com.zerobase.user.domain.model.Customer;
import com.zerobase.user.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Optional<Customer> findByIdAndEmail(Long id, String email) {
        return customerRepository.findById(id)
                .stream().filter(customer -> email.equals(customer.getEmail()))
                .findFirst();
    }

    public Optional<Customer> findValidCustomer(String email, String password) {
        return customerRepository.findByEmail(email)
                .stream()
                .filter(
                        customer -> password.equals(customer.getPassword())
                                && customer.isVerify()
                ).findFirst();
    }
}
