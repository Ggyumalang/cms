package com.zerobase.user.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumValidator.class)
public @interface PhoneNumCheck {

    String message() default "잘못된 휴대폰 번호입니다.";

    Class[] groups() default {};

    Class[] payload() default {};
}
