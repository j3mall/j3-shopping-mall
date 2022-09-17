package com.j3mall.user;

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
@MapperScan("com.j3mall.user.mybatis.mapper")
public class UserServiceApplication {

	@Value("${spring.application.name}:${server.port}")
	private String serverHost;

	@GetMapping("/")
	public String home() {
		return "Hello, here is " + serverHost;
	}

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
