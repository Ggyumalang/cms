package com.zerobase.cms.order.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class DeployTestController {
    public String hello(){
        return "hello Order Api";
    }
}
