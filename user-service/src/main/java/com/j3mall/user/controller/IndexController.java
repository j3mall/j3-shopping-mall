package com.j3mall.user.controller;

import com.j3mall.j3.framework.utils.MyBeanUtils;
import io.swagger.annotations.ApiParam;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class IndexController {

    @Resource
    private ApplicationContext applicationContext;

    @Value("${spring.application.name}:${server.port}")
    private String serverHost;

    @GetMapping("/")
    public String home() {
        return "Hello, here is " + serverHost;
    }

    @GetMapping("/allBean")
    public Map<String, String> allBean(
        @ApiParam(value = "名称") @RequestParam(value = "name") String name) {
        return MyBeanUtils.findBeans(applicationContext, name);
    }

}
