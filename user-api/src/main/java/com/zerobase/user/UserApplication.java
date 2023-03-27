package com.zerobase.user;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ServletComponentScan
@EnableFeignClients
@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories
public class UserApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "classpath:application-mailgun.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(UserApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}