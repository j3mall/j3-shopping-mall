package com.j3mall.user.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录拦截器
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 登录判断业务
        HttpSession session = request.getSession(false);
        if (null == session || session.getAttribute("userInfo") != null) {
            return true;
        }
        log.error("当前用户没有权限访问 [{} {}]", request.getMethod(), request.getRequestURI());
        response.setStatus(401);
        return false;
    }
}

