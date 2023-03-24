package com.zerobase.user.controller;

import com.zerobase.user.client.MailgunClient;
import com.zerobase.user.client.mailgun.SendMailForm;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final MailgunClient mailgunClient;

    @GetMapping
    public String sendTestEmail() {
        SendMailForm form = SendMailForm.builder()
                .from("zerobase-test@gmail.com")
                .to("khg50877@gmail.com")
                .subject("test")
                .text("test Message")
                .build();
        return mailgunClient.sendEmail(form).getBody();
    }
}
