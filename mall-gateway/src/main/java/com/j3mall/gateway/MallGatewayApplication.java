package com.j3mall.gateway;

import com.j3mall.framework.cache.EnableCachePlugins;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableCachePlugins(redissonSupport = true)
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MallGatewayApplication {

    @Value("${spring.application.name}:${server.port}")
    private String serverHost;

    @GetMapping("/")
    public String home() {
        return "J3Mall网关欢迎你, " + serverHost;
    }

    public static void main(String[] args) {
        SpringApplication.run(MallGatewayApplication.class, args);
    }

}
