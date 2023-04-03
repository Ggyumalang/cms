package com.zerobase.user.domain.customer;

import com.zerobase.user.domain.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerDto {

    private Long id;
    private String email;
    private String name;
    private Long balance;

    public static CustomerDto from(Customer customer) {
        return new CustomerDto(customer.getId(), customer.getEmail(),
            customer.getName(),
            customer.getBalance() == null ? 0 : customer.getBalance());
    }
}
