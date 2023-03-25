package com.j3mall.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.j3mall.user", "com.j3mall.framework"})
@MapperScan("com.j3mall.user.mybatis.mapper")
public class UserServiceApplication extends SpringBootServletInitializer {
	@Value("${spring.application.name}:${server.port}")
	private String serverHost;

	@GetMapping("/")
	public String home() {
		return "Hello, here is " + serverHost;
	}

	public static void main(String[] args) {
		Long time=System.currentTimeMillis();
		SpringApplication.run(UserServiceApplication.class, args);
		System.out.printf("===应用启动耗时：%s毫秒===%n", System.currentTimeMillis() - time);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(this.getClass());
	}
}
