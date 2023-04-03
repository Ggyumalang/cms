package com.zerobase.user.domain;

import com.zerobase.user.validator.PhoneNumCheck;
import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {

    @Email
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate birth;

    @NotBlank
    @PhoneNumCheck
    private String phone;
}
