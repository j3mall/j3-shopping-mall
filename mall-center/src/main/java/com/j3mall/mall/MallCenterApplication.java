package com.j3mall.mall;

import com.j3mall.modules.feign.order.OrderFeignService;
import com.j3mall.modules.feign.product.ProductFeginService;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 聚合多个微服务，对外提供接口
 */
@EnableDubbo
@RestController
@EnableDiscoveryClient
@EnableAspectJAutoProxy(proxyTargetClass = false)
@SpringBootApplication(scanBasePackages = {"com.j3mall.mall", "com.j3mall.annotation"})
@EnableFeignClients(clients = {
    ProductFeginService.class,
    OrderFeignService.class
})
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
