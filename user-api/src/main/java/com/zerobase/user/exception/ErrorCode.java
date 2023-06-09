package com.zerobase.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    HTTP_MESSAGE_NOT_READABLE(HttpStatus.BAD_REQUEST, ""),

    NOT_ENOUGH_BALANCE(HttpStatus.BAD_REQUEST, "잔액이 부족합니다."),
    WRONG_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "잘못된 인증 코드 입니다."),
    EXPIRED_VERIFICATION(HttpStatus.BAD_REQUEST, "인증 시간이 만료되었습니다."),
    ALREADY_VERIFIED_USER(HttpStatus.BAD_REQUEST, "이미 인증이 완료된 회원입니다."),
    ALREADY_REGISTERED_USER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "일치하는 회원이 없습니다."),

    //login
    LOGIN_CHECK_FAIL(HttpStatus.BAD_REQUEST, "아이디나 패스워드를 확인해주세요.");

    private final HttpStatus httpStatus;
    private final String detail;
}
