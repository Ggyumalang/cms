package com.zerobase.user.service.customer;

import static com.zerobase.user.exception.ErrorCode.NOT_ENOUGH_BALANCE;
import static com.zerobase.user.exception.ErrorCode.NOT_FOUND_USER;

import com.zerobase.user.domain.customer.ChangeBalanceForm;
import com.zerobase.user.domain.model.CustomerBalanceHistory;
import com.zerobase.user.domain.repository.CustomerBalanceHistoryRepository;
import com.zerobase.user.domain.repository.CustomerRepository;
import com.zerobase.user.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomerBalanceService {

    private final CustomerBalanceHistoryRepository customerBalanceHistoryRepository;
    private final CustomerRepository customerRepository;

    @Transactional(noRollbackFor = {CustomException.class})
    public CustomerBalanceHistory changeBalance(Long customerId,
        ChangeBalanceForm form) throws CustomException {
        CustomerBalanceHistory customerBalanceHistory =
            customerBalanceHistoryRepository.findFirstByCustomer_IdOrderByIdDesc(
                    customerId)
                .orElse(CustomerBalanceHistory.builder()
                    .changedMoney(0L)
                    .currentMoney(0L)
                    .customer(customerRepository.findById(customerId)
                        .orElseThrow(() -> new CustomException(NOT_FOUND_USER))
                    )
                    .build());

        if (customerBalanceHistory.getCurrentMoney() + form.getMoney() < 0) {
            throw new CustomException(NOT_ENOUGH_BALANCE);
        }

        customerBalanceHistory = CustomerBalanceHistory.builder()
            .changedMoney(form.getMoney())
            .currentMoney(
                customerBalanceHistory.getCurrentMoney() + form.getMoney())
            .description(form.getMessage())
            .fromMessage(form.getFrom())
            .customer(customerBalanceHistory.getCustomer())
            .build();

        customerBalanceHistory.getCustomer()
            .setBalance(customerBalanceHistory.getCurrentMoney());

        return customerBalanceHistoryRepository.save(customerBalanceHistory);
    }


}
