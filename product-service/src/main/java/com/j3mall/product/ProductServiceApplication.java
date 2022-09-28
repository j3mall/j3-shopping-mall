package com.j3mall.product;

import com.j3mall.modules.feign.user.UserFeignService;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableDubbo
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.j3mall.product", "com.j3mall.annotation.log"})
@MapperScan("com.j3mall.product.mybatis.mapper")
@EnableFeignClients(clients = {
        UserFeignService.class
})
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
