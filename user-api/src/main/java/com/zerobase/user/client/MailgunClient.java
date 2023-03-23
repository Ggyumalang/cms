package com.zerobase.user.client;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "mailgun" , url = "https://api.malgun.net/v3/")
@Qualifier("mailgun")
public class MailgunClient {

    @Value(value = "${mailgun.key}")
    private String mailgunKey;

    @PostMapping
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(){

    }
}
