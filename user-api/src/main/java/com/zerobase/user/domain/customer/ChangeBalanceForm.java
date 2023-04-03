package com.zerobase.user.domain.customer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeBalanceForm {

    @NotBlank
    private String from;

    private String message;

    @NotNull
    private Long money;

}
