package com.j3mall.user;

import com.j3mall.framework.datasource.EnableDynamicDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDynamicDataSource
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.j3mall.user", "com.j3mall.framework"})
@MapperScan("com.j3mall.user.mybatis.mapper")
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
