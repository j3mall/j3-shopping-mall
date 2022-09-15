package com.j3mall.user.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {

    @Value("${spring.application.name}:${server.port}")
    private String serverHost;

    @GetMapping("/")
    public String home() {
        return "Hello, here is " + serverHost;
    }

}
