package com.zerobase.user.client.service;

import com.zerobase.user.client.MailgunClient;
import com.zerobase.user.client.mailgun.SendMailForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailSendServiceTest {

    @Autowired
    private MailgunClient mailgunClient;

    @Test
    void EmailTest() {
        //given
        SendMailForm form = SendMailForm.builder()
                .from("zerobase-test@gmail.com")
                .to("khg50877@gmail.com")
                .subject("test")
                .text("test Message")
                .build();
        //when
        String response = mailgunClient.sendEmail(form).getBody();
        System.out.println(response);
    }

}