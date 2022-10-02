package com.j3mall.order;

import com.j3mall.modules.feign.product.ProductFeginService;
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

@EnableDubbo
@RestController
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.j3mall.order", "com.j3mall.annotation.log"})
@MapperScan("com.j3mall.order.mybatis.mapper")
@EnableFeignClients(clients = {
        UserFeignService.class,
        ProductFeginService.class
})
public class OrderServiceApplication {

    @Value("${spring.application.name}:${server.port}")
    private String serverHost;

    @GetMapping("/")
    public String home() {
        return "Hello, here is " + serverHost;
    }

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

}
