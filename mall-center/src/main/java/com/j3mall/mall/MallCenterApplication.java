package com.j3mall.mall;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 聚合多个微服务，对外提供接口
 */
@RestController
@SpringBootApplication
public class MallCenterApplication {
    @Value("${j3.platform}")
    private String platform;
    @Value("${spring.application.name}:${server.port}")
    private String serverHost;

    @GetMapping("/")
    public String home() {
        return platform + "为你提供聚合服务, " + serverHost;
    }

    public static void main(String[] args) {
        SpringApplication.run(MallCenterApplication.class, args);
    }
}
