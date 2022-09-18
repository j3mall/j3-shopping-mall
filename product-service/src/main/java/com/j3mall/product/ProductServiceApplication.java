package com.j3mall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.j3mall.product.mybatis.mapper")
public class ProductServiceApplication {

    @Value("${spring.application.name}:${server.port}")
    private String serverHost;

    @GetMapping("/")
    public String home() {
        return "Hello, here is " + serverHost;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

}
