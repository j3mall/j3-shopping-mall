package com.j3mall.user.config;

import com.j3mall.user.interceptor.LoginInterceptor;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    /** 不拦截的 url 集合 **/
    List<String> excludes = new ArrayList<String>() {{
        add("/**/*.html");
        add("/js/**");
        add("/css/**");
        add("/img/**"); // 放行 static/img 下的所有文件
        add("/api/users/login"); // 放行登录接口
        add("/api/users/register"); // 放行注册接口
    }};

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 配置拦截器
        InterceptorRegistration registration = registry.addInterceptor(loginInterceptor);
        registration.addPathPatterns("/**");
        registration.excludePathPatterns(excludes);
    }

    // 所有的接⼝添加 api 前缀
    // @Override
    // public void configurePathMatch(PathMatchConfigurer configurer) {
    //     configurer.addPathPrefix("api", c -> true);
    // }

}
