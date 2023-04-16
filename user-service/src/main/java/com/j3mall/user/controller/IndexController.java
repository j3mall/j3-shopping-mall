package com.j3mall.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.j3mall.j3.framework.utils.JsonResult;
import com.j3mall.j3.framework.utils.MyBeanUtils;
import io.swagger.annotations.ApiParam;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class IndexController {

    @Resource
    RedisTemplate<String, Object> redisTemplate;
    @Resource
    RedisTemplate<String, Object> jsonRedisTemplate;

    @Resource
    ApplicationContext applicationContext;

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

    @GetMapping("/writeRedis")
    public JsonResult writeRedis(@RequestParam(value = "key") String key, @RequestParam(value = "data", required = false) String data) {
        Optional.ofNullable(data).ifPresent(value -> {
            String jsonValue = String.format("{\"key\":\"%s\",\"data\":\"%s\"}", key, value);
            redisTemplate.opsForValue().set(key + "-default", jsonValue);
            jsonRedisTemplate.opsForValue().set(key, jsonValue);
        });
        Object object = jsonRedisTemplate.opsForValue().get(key);
        JSONObject jsonObject = JSON.parseObject(Optional.ofNullable(object).orElse("{}").toString());
        return JsonResult.success(jsonObject);
    }

}
